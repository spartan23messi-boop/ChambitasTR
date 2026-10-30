package com.leonardo.almacen.entities;

import com.leonardo.almacen.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "SUCURSALES")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;
    @Column(name = "NOMBRE", length = 50, nullable = false,unique = true)
    private String nombre;
    @Column(name = "DIRECCION", length = 150,nullable = false)
    private String direccion;


    private static void validarDatos(String nombre, String direccion){
        StringCustomUtils.validarTamanio(nombre,5,50,"El nombre debe tener entre 5 y 50 caracteres");
        StringCustomUtils.validarTamanio(direccion,10,150,"La dirección debe contener entre 10 y 150 caracteres");
    }

    public void actualizar (String nombre,String direccion){
        validarDatos(nombre,direccion);
        this.nombre=nombre.trim();
        this.direccion=direccion.trim();
    }

    public static Sucursal crear(String nombre, String direccion){
        validarDatos(nombre,direccion);
        return Sucursal.builder()
                .nombre(nombre.trim())
                .direccion(direccion.trim())
                .build();
    }
}
