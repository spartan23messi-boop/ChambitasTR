package com.leonardo.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar una sucursal")
public record SucursalRequest(
        @Schema(description = "Nombre de la sucursal", example = "Puebla")
        @NotBlank(message = "La sucursal es requerida")
        @Size (min = 5,max = 50,message = "El nombre de la sucursal debe contener etre 5 y 50 caracteres")
        String nombre,
        @Schema(description = "Dirección de la sucursal", example = "Torres Landa #280")
        @NotBlank(message = "La dirección de la sucursal es requerida")
        @Size (min = 10,max = 150,message = "La dirección de la sucursal debe contener etre 10 y 150 caracteres")
        String direccion
){}
