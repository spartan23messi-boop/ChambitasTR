package com.leonardo.almacen.services.productos;

import com.leonardo.almacen.dto.productos.ProductoRequest;
import com.leonardo.almacen.dto.productos.ProductoResponse;
import com.leonardo.almacen.entities.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<ProductoResponse> listar(
            String nombre,
            String categoria,
            BigDecimal precioMin,
            BigDecimal precioMax
    );

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse registrar(ProductoRequest request);

    ProductoResponse actualizar(ProductoRequest request, Long id);

    void eliminar(Long id);
}
