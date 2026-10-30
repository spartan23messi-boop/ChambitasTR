package com.leonardo.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Detalle de un producto dentro de una venta")
public record DetalleVentaResponse(
        @Schema(description = "ID del producto", example = "1")
        Long idProducto,
        @Schema(description = "Nombre del producto", example = "Laptop Gamer")
        String nombreProducto,
        @Schema(description = "Cantidad de unidades Vendidas", example = "10")
        Integer cantidadProducto,
        @Schema(description = "Precio del producto", example = "15999.99")
        String precioProducto,
        @Schema(description = "Subtotal del producto", example = "15000.00")
        BigDecimal subtotal
) {}
