// backend/src/main/java/com/fyztech/tienda/repository/PagoRepository.java
package com.fyztech.tienda.repository;

import com.fyztech.tienda.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {
    
    Optional<Pago> findByOrdenId(UUID ordenId);
    
    List<Pago> findByEstado(String estado);
    
    List<Pago> findByEstadoOrderByFechaCreacionDesc(String estado);
    
    @Query("SELECT p FROM Pago p WHERE p.orden.id = :ordenId")
    Optional<Pago> encontrarPorOrdenId(@Param("ordenId") UUID ordenId);
}