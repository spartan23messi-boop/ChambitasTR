package com.leonardo.almacen.services.ventas;

import com.leonardo.almacen.dto.ventas.DetalleVentaRequest;
import com.leonardo.almacen.dto.ventas.VentaRequest;
import com.leonardo.almacen.dto.ventas.VentaResponse;
import com.leonardo.almacen.entities.DetalleVenta;
import com.leonardo.almacen.entities.Producto;
import com.leonardo.almacen.entities.Sucursal;
import com.leonardo.almacen.entities.Venta;
import com.leonardo.almacen.enums.EstadoVenta;
import com.leonardo.almacen.exceptions.ConflictoException;
import com.leonardo.almacen.exceptions.DatoInvalidoException;
import com.leonardo.almacen.exceptions.RecursoNoEncontradoException;
import com.leonardo.almacen.mappers.VentaMapper;
import com.leonardo.almacen.repositories.ProductoRepository;
import com.leonardo.almacen.repositories.SucursalRepository;
import com.leonardo.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaServiceImpl implements ventaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;

    // REGISTRAR VENTA: TODO SE GUARDA O TODO SE REVIERTE
    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {
        if (request == null || request.productos() == null || request.productos().isEmpty()) {
            throw new DatoInvalidoException("Debes incluir productos en la venta.");
        }
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la sucursal."));

        Venta venta = Venta.builder()
                .sucursal(sucursal)
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .build();

        // VALIDAR PRODUCTOS, DESCONTAR STOCK Y GUARDAR PRECIO ACTUAL
        Set<Long> ids = new HashSet<>();
        for (DetalleVentaRequest item : request.productos()) {
            if (item == null || item.idProducto() == null || item.cantidadProducto() == null
                    || item.cantidadProducto() <= 0) {
                throw new DatoInvalidoException("Cada detalle necesita producto y cantidad positiva.");
            }
            if (!ids.add(item.idProducto())) {
                throw new DatoInvalidoException("No repitas un producto dentro de la venta.");
            }
            Producto producto = productoRepository.bloquearPorId(item.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el producto " + item.idProducto()));
            producto.descontarCantidad(item.cantidadProducto());
            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(item.cantidadProducto())
                    .precioProducto(producto.getPrecio())
                    .build();
            venta.agregarDetalle(detalle);
        }
        ventaRepository.saveAndFlush(venta);
        log.info("Venta {} registrada con {} productos", venta.getId(), venta.getDetalleVentas().size());
        return ventaMapper.entidadAResponse(venta);
    }

    // LISTAR ÚNICAMENTE VENTAS ACTIVAS
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return listarPorEstado(EstadoVenta.REGISTRADA);
    }

    // CONSULTAR UNA VENTA ACTIVA POR ID
    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        Venta venta = ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una venta activa con ese ID."));
        return ventaMapper.entidadAResponse(venta);
    }

    // CANCELAR Y DEVOLVER EL STOCK (EN UNA SOLA TRANSACCIÓN)
    @Override
    @Transactional
    public void cancel(Long id) {
        Venta venta = ventaRepository.bloquearVentaPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la venta."));
        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new ConflictoException("La venta ya está cancelada.");
        }
        // Bloquear productos en orden estable para evitar bloqueos cruzados.
        venta.getDetalleVentas().stream()
                .sorted((a, b) -> a.getProducto().getId().compareTo(b.getProducto().getId()))
                .forEach(detalle -> {
                    Producto producto = productoRepository.bloquearPorId(detalle.getProducto().getId())
                            .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado."));
                    producto.aumentarCantidad(detalle.getCantidadProducto());
                });
        venta.cancelar();
        log.info("Venta {} cancelada; stock devuelto", id);
    }

    // CONSULTAR HISTÓRICO DE VENTAS CANCELADAS
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        return listarPorEstado(EstadoVenta.CANCELADA);
    }

    // REUTILIZAR LA TRANSFORMACIÓN PARA AMBOS LISTADOS
    private List<VentaResponse> listarPorEstado(EstadoVenta estado) {
        return ventaRepository.findByEstadoVenta(estado).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }
}
