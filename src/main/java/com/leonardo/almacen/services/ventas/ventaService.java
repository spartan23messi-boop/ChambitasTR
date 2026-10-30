package com.leonardo.almacen.services.ventas;

import com.leonardo.almacen.dto.ventas.VentaRequest;
import com.leonardo.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface ventaService {
    List<VentaResponse> listar();
    List<VentaResponse> listarCanceladas();
    VentaResponse obtenerPorIdActiva(Long id);
    VentaResponse registrar(VentaRequest request);
    void cancel (Long id);
}
