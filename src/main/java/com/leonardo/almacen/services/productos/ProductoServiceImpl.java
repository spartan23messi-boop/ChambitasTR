package com.leonardo.almacen.services.productos;

import com.leonardo.almacen.dto.productos.ProductoRequest;
import com.leonardo.almacen.dto.productos.ProductoResponse;
import com.leonardo.almacen.entities.Producto;
import com.leonardo.almacen.enums.Categoria;
import com.leonardo.almacen.exceptions.DatoInvalidoException;
import com.leonardo.almacen.exceptions.RecursoNoEncontradoException;
import com.leonardo.almacen.mappers.ProductoMapper;
import com.leonardo.almacen.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // en caso de que algún proceso falle hace un roll back para que los datos no queden incoherentes
@Slf4j//Simple loging facade for java-> crea un logger en la consola
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre,
            String categoria,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        // CONSULTAR FILTROS EN ORACLE Y TRANSFORMAR LOS RESULTADOS
        return buscarProductos(nombre, categoria, precioMin, precioMax).stream()
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProducto(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim())
        );
        productoRepository.saveAndFlush(producto);
        log.info("Nuevo producto {} registrado",producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {

        Producto producto = obtenerProducto(id);
        log.info("Actualizando producto con id: {}", id);
        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad()
                );
        productoRepository.saveAndFlush(producto);
        log.info("Producto con id {} actualizado" , id);
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {

        Producto producto= obtenerProducto(id);
        log.info("Eliminando producto con id: {}", id);
        productoRepository.delete(producto);
        productoRepository.flush();
        log.info("Producto con id {} eliminado",id);
    }

    // PREPARAR LOS FILTROS OPCIONALES Y CONSULTAR ORACLE CON JPQL
    private List<Producto> buscarProductos(
            String nombre,
            String categoria,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        // IGNORAR EL NOMBRE SI ES NULO, VACÍO O SOLO CONTIENE ESPACIOS
        String nombreFiltro = (nombre == null || nombre.isBlank())
                ? null : nombre.trim();

        // CONVERTIR CATEGORÍA; SI NO EXISTE, IGNORAR ESTE FILTRO
        Categoria categoriaFiltro = null;
        if (categoria != null && !categoria.isBlank()) {
            try {
                categoriaFiltro = Categoria.obtenerCategoriaPorDescripcion(categoria.trim());
            } catch (DatoInvalidoException e) {
                log.warn("Categoría no válida: {}. Se ignorará el filtro.", categoria);
            }
        }

        // APLICAR TODOS LOS FILTROS EN UNA SOLA CONSULTA A LA BASE DE DATOS
        return productoRepository.buscarConFiltros(
                nombreFiltro, categoriaFiltro, precioMin, precioMax);
    }

    private Producto obtenerProducto(Long id){
        log.info("Buscando producto con id: {}" , id);

        return productoRepository.findById(id).
                orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id)
        );
    }


}
