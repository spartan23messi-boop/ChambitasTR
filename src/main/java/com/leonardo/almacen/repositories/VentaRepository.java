package com.leonardo.almacen.repositories;

import com.leonardo.almacen.entities.Venta;
import com.leonardo.almacen.enums.EstadoVenta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    // FILTRAR VENTAS POR ESTADO Y CARGAR DATOS PARA EL MAPPER
    @EntityGraph(attributePaths = {"sucursal", "detalleVentas", "detalleVentas.producto"})
    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);

    @EntityGraph(attributePaths = {"sucursal", "detalleVentas", "detalleVentas.producto"})
    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);

    // BLOQUEAR VENTA AL CANCELAR PARA EVITAR DOBLE DEVOLUCIÓN DE STOCK
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Venta v WHERE v.id = :id")
    Optional<Venta> bloquearVentaPorId(@Param("id") Long id);
}
