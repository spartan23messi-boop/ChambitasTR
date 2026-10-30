package com.leonardo.almacen.services.sucursales;

import com.leonardo.almacen.dto.sucursales.SucursalRequest;
import com.leonardo.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar();
    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);
}
