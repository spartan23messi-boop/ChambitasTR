package com.leonardo.almacen.dto.ventas;

import com.leonardo.almacen.dto.sucursales.SucursalResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record VentaResponse(
        @Schema(description = "ID de la venta", example = "1")
        Long id,
        @Schema(description = "Fecha de la venta", example = "02/09/2026")
        String fecha,
        @Schema(description = "Estado de la venta", example = "Registrada")
        String estado,
        @Schema(description = "Sucursal en la que fue vendido")
        SucursalResponse sucursal,
        @Schema(description = "Lista de productos de la venta")
        List<DetalleVentaResponse> detalles,
        @Schema(description = "Total de la venta", example = "15000.00")
        BigDecimal total
) {}
