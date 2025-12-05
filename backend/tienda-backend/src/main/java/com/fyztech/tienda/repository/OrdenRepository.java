package com.fyztech.tienda.repository;

import com.fyztech.tienda.model.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, UUID> {
    
    List<Orden> findByUsuarioIdOrderByFechaCreacionDesc(UUID usuarioId);
    
    List<Orden> findByEstado(String estado);
    
    @Query("SELECT o FROM Orden o WHERE o.usuario.id = :usuarioId AND o.estado = :estado")
    List<Orden> findByUsuarioIdAndEstado(@Param("usuarioId") UUID usuarioId, 
                                         @Param("estado") String estado);
}