package com.leonardo.almacen.mappers;

import com.leonardo.almacen.dto.sucursales.SucursalRequest;
import com.leonardo.almacen.dto.sucursales.SucursalResponse;
import com.leonardo.almacen.entities.Sucursal;
import com.leonardo.almacen.repositories.SucursalRepository;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public Sucursal requestAEntidad(SucursalRequest request){
        return request == null
                ? null
                : Sucursal.crear(
                    request.nombre(),
                    request.direccion()
                );
    }
    public SucursalResponse entidadAResponse(Sucursal sucursal){
        return sucursal == null
                ? null
                : new SucursalResponse(
                    sucursal.getId(),
                    sucursal.getNombre(),
                    sucursal.getDireccion()
                );
    }
}
