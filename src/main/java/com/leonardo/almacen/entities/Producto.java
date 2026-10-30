package com.leonardo.almacen.entities;

import com.leonardo.almacen.enums.Categoria;
import com.leonardo.almacen.exceptions.DatoInvalidoException;
import com.leonardo.almacen.utils.StringCustomUtils;
import com.leonardo.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "PRODUCTOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//especifica la forma en que se crea el id
    @Column(name = "ID_PRODUCTO")
    private Long id;
    @Column(name = "NOMBRE", nullable = false,length = 30)
    private String nombre;
    @Column(name = "CATEGORIA", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)//Sirve para que guarde el String equivalente y no la posición en la que lo encuentra
    private Categoria categoria;
    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    private static void validarDatos(String nombre, Categoria categoria, BigDecimal precio, Integer cantidad) {
        StringCustomUtils.validarTamanio(nombre,5,30,"El nombre debe tener entre 5 y 30 caracteres");
        if(categoria== null)
            throw new DatoInvalidoException("La categoria es requerida");
        ValoresNumericosUtils.validarNumeroRequerido(cantidad,"La cantidad es requerida");
        ValoresNumericosUtils.validarEnteroPositivo(cantidad,"La cantidad debe ser positiva");
    }

    public void actualizar(String nombre, Categoria categoria, BigDecimal precio, Integer cantidad) {
        validarDatos(nombre,categoria,precio,cantidad);
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public void aumentarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(cantidad,"La cantidad debe ser positiva");
        this.cantidad+=cantidad;
    }

    public void descontarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(cantidad,"La cantidad debe ser positiva");
        if(cantidad>this.cantidad)
            throw new DatoInvalidoException("La cantidad debe ser menor o igual a la cantidad actual");
        this.cantidad-=cantidad;
    }

    public static Producto crear(String nombre, Categoria categoria, BigDecimal precio, Integer cantidad) {
        validarDatos(nombre,categoria,precio,cantidad);
        return  Producto.builder()
                .nombre(nombre.trim())
                .categoria(categoria)
                .precio(precio)
                .cantidad(cantidad)
                .build();
    }
}
