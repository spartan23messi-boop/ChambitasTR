package com.leonardo.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description ="Información de una sucursal")
public record SucursalResponse(
    @Schema (description = "ID único de la sucursal", example = "1")
    Long id,
    @Schema (description = "Nombre de la sucursal", example = "Puebla")
    String nombre,
    @Schema (description = "Direccion de la sucursal", example = "Torres Landa #280")
    String direccion
){}
