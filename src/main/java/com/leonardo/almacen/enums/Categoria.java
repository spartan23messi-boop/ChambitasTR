package com.leonardo.almacen.enums;

import com.leonardo.almacen.exceptions.DatoInvalidoException;
import com.leonardo.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {
    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Juguete"),
    ELECTRONICA("Electrónica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");
    private final String descripcion;
    public static Categoria obtenerCategoriaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion,"La descripción es requerida");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);
        for (Categoria categoria : values()){
            if(StringCustomUtils.normalizarTexto(categoria.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return categoria;
        }
        throw new DatoInvalidoException("No existe una categoría en la descripción:" +descripcion );
    }
}
