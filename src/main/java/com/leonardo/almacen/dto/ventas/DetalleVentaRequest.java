package com.leonardo.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Detalle de un producto de la venta")
public record DetalleVentaRequest(
        @Schema(description = "ID del producto", example = "1")
        @NotNull(message = "El id es requerido.")
        @Positive(message = "El id debe ser positivo")
        Long idProducto,
        @Schema(description = "Cantidad del producto", example = "10")
        @NotNull(message = "La cantidad es requerida.")
        @Positive(message = "La cantidad debe ser positiva.")
        Integer cantidadProducto
) {

}
