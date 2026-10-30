package com.leonardo.almacen.controllers;

import com.leonardo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.leonardo.almacen.services.reportes.ReporteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "API Reportes", description = "Reporte financiero de ventas por sucursal")
public class ReporteController {
    private final ReporteService reporteService;

    // CONSULTAR FACTURACION Y ARTICULOS VENDIDOS POR SUCURSAL
    @GetMapping("/ventas-sucursales")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReporte() {
        return ResponseEntity.ok(reporteService.obtenerReporteVentas());
    }
}
