package com.example.biobanksystem.repository;

import com.example.biobanksystem.model.SampleBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoxRepository extends JpaRepository<SampleBox, Long> {
    // Basic CRUD methods are inherited
    // For example: List<SampleBox> findByDrawerId(Long drawerId);
}
