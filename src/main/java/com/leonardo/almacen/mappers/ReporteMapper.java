package com.leonardo.almacen.mappers;

import com.leonardo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.leonardo.almacen.repositories.projections.ReporteVentasSucursalProjection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReporteMapper {

    // CONVERTIR LA PROJECTION AL DTO DE RESPUESTA
    public ReporteVentasSucursalResponse projectionAResponse(ReporteVentasSucursalProjection reporte) {
        BigDecimal total = reporte.getTotalFacturado() == null
                ? BigDecimal.ZERO : reporte.getTotalFacturado();
        Long unidades = reporte.getCantidadProductosVendidos() == null
                ? 0L : reporte.getCantidadProductosVendidos();
        return new ReporteVentasSucursalResponse(
                reporte.getIdSucursal(), reporte.getNombreSucursal(), total, unidades);
    }
}
