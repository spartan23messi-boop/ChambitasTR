package com.leonardo.almacen.dto.reportes;

import java.math.BigDecimal;

// RESPUESTA DEL REPORTE POR SUCURSAL
public record ReporteVentasSucursalResponse(
        Long idSucursal,
        String nombreSucursal,
        BigDecimal totalFacturado,
        Long cantidadProductosVendidos
) {}
