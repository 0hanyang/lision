package com.example.biobanksystem.repository;

import com.example.biobanksystem.model.BoxGrid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxGridRepository extends JpaRepository<BoxGrid, Long> {
    // Basic CRUD methods are inherited
    // For example: List<BoxGrid> findBySampleBoxId(Long sampleBoxId);
}
