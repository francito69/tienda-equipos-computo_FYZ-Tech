package com.fyztech.tienda.repository;

import com.fyztech.tienda.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(UUID categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Producto> buscarProductos(@Param("busqueda") String busqueda);
    
    List<Producto> findByStockGreaterThanAndActivoTrue(Integer stock);
    
    Optional<Producto> findByIdAndActivoTrue(UUID id);
}