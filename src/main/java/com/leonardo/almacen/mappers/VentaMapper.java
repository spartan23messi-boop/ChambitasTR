package com.leonardo.almacen.mappers;

import com.leonardo.almacen.dto.ventas.DetalleVentaResponse;
import com.leonardo.almacen.dto.ventas.VentaResponse;
import com.leonardo.almacen.entities.DetalleVenta;
import com.leonardo.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {
    private final SucursalMapper sucursalMapper;

    // CONVERTIR UN DETALLE Y CALCULAR SU SUBTOTAL
    public DetalleVentaResponse detalleAResponse(DetalleVenta detalle) {
        BigDecimal subtotal = detalle.getPrecioProducto()
                .multiply(BigDecimal.valueOf(detalle.getCantidadProducto()));
        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto().toPlainString(),
                subtotal
        );
    }

    // CONVERTIR UNA VENTA A DTO Y SUMAR SUS SUBTOTALES
    public VentaResponse entidadAResponse(Venta venta) {
        List<DetalleVentaResponse> detalles = venta.getDetalleVentas().stream()
                .map(this::detalleAResponse)
                .toList();
        BigDecimal total = detalles.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detalles,
                total
        );
    }
}
