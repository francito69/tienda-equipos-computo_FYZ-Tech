package com.fyztech.tienda.repository;

import com.fyztech.tienda.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {
    
    Optional<Pago> findByOrdenId(UUID ordenId);
    
    List<Pago> findByEstado(String estado);
    
    List<Pago> findByMetodoPagoAndEstado(String metodoPago, String estado);
}