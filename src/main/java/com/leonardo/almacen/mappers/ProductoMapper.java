package com.leonardo.almacen.mappers;

import com.leonardo.almacen.dto.productos.ProductoRequest;
import com.leonardo.almacen.dto.productos.ProductoResponse;
import com.leonardo.almacen.entities.Producto;
import com.leonardo.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){
        return request == null
            ? null
            :Producto.crear(
                 request.nombre(),
                 categoria,
                 request.precio(),
                 request.cantidad());
    }

    public ProductoResponse entidadAResponse(Producto producto){
        return producto == null
                ? null
                : new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad());
    }
}
