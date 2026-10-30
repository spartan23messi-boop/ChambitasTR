package com.leonardo.almacen.repositories.projections;

import java.math.BigDecimal;

// DATOS AGREGADOS QUE OBTENEMOS DIRECTAMENTE DE LA BDD
public interface ReporteVentasSucursalProjection {
    Long getIdSucursal();
    String getNombreSucursal();
    BigDecimal getTotalFacturado();
    Long getCantidadProductosVendidos();
}
