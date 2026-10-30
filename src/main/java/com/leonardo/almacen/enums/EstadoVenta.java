package com.leonardo.almacen.enums;

import com.leonardo.almacen.exceptions.DatoInvalidoException;
import com.leonardo.almacen.utils.StringCustomUtils;
import com.leonardo.almacen.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {
    REGISTRADA(1L,"Registrada"),
    CANCELADA(0L,"Cancelada");
    private final Long codigo;
    private final String descripcion;
    public static EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion,"La descripción es requerida");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);
        for (EstadoVenta estadoVenta : values()){
            if(StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }
        throw new DatoInvalidoException("No existe un estado de venta con la descripción:" +descripcion );
    }
    public static EstadoVenta obtenerEstadoVentaPorDescripcion(Long codigo){
        ValoresNumericosUtils.validarNumeroRequerido(codigo,"El código es requerido");
        for (EstadoVenta estadoVenta : values()){
            if (estadoVenta.codigo.equals(codigo))
                return estadoVenta;
        }
        throw new DatoInvalidoException("No existe un estado de venta con el código:" + codigo );
    }
}
