package com.example.biobanksystem.repository;

import com.example.biobanksystem.model.FreezerLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreezerLayerRepository extends JpaRepository<FreezerLayer, Long> {
    // Basic CRUD methods are inherited
    // Custom query methods can be added here if needed
    // For example: List<FreezerLayer> findByRefrigeratorId(Long refrigeratorId);
}
