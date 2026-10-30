package com.leonardo.almacen.repositories;

import com.leonardo.almacen.entities.Sucursal;
import com.leonardo.almacen.enums.EstadoVenta;
import com.leonardo.almacen.repositories.projections.ReporteVentasSucursalProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    //SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(:NOMBRE)
    boolean existsByNombreIgnoreCase(String nombre);
    //SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(:NOMBRE) AND ID_SUCURSAL <> :ID
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre,Long id);
    // AGREGAR IMPORTES Y CANTIDADES POR SUCURSAL (EXCLUYE VENTAS CANCELADAS)
    // LEFT JOIN PERMITE INCLUIR SUCURSALES SIN VENTAS CON TOTALES EN CERO.
    @Query("""
            SELECT s.id AS idSucursal,
                   s.nombre AS nombreSucursal,
                   COALESCE(SUM(d.cantidadProducto * d.precioProducto), 0) AS totalFacturado,
                   COALESCE(SUM(d.cantidadProducto), 0L) AS cantidadProductosVendidos
            FROM Sucursal s
            LEFT JOIN Venta v ON v.sucursal = s AND v.estadoVenta = :estado
            LEFT JOIN v.detalleVentas d
            GROUP BY s.id, s.nombre
            ORDER BY s.id
            """)
    List<ReporteVentasSucursalProjection> obtenerReporteVentas(@Param("estado") EstadoVenta estado);
}
