package com.leonardo.almacen.repositories;

import com.leonardo.almacen.entities.Producto;
import com.leonardo.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // FILTRAR PRODUCTOS: cada parámetro nulo se ignora de forma independiente.
    @Query("""
            SELECT p FROM Producto p
            WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', CONCAT(:nombre, '%'))))
              AND (:categoria IS NULL OR p.categoria = :categoria)
              AND (:precioMin IS NULL OR p.precio >= :precioMin)
              AND (:precioMax IS NULL OR p.precio <= :precioMax)
            """)
    List<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax);

    // BLOQUEAR PRODUCTO DURANTE EL DESCUENTO O DEVOLUCIÓN DE STOCK
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> bloquearPorId(@Param("id") Long id);
}
