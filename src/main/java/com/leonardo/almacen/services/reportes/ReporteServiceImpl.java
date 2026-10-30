package com.leonardo.almacen.services.reportes;

import com.leonardo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.leonardo.almacen.enums.EstadoVenta;
import com.leonardo.almacen.mappers.ReporteMapper;
import com.leonardo.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {
    private final SucursalRepository sucursalRepository;
    private final ReporteMapper reporteMapper;

    // CONSULTAR SOLO VENTAS REGISTRADAS Y TRANSFORMAR A DTO
    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerReporteVentas() {
        log.info("Generando reporte de ventas activas agrupadas por sucursal");
        return sucursalRepository.obtenerReporteVentas(EstadoVenta.REGISTRADA)
                .stream()
                .map(reporteMapper::projectionAResponse)
                .toList();
    }
}
