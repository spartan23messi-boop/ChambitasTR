package com.leonardo.almacen.services.reportes;

import com.leonardo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import java.util.List;

public interface ReporteService {
    // CONSULTAR EL REPORTE FINANCIERO POR SUCURSAL
    List<ReporteVentasSucursalResponse> obtenerReporteVentas();
}
