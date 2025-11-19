package com.fyztech.tienda.repository;

import com.fyztech.tienda.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}