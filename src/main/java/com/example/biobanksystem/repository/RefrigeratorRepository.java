package com.example.biobanksystem.repository;

import com.example.biobanksystem.model.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    // Basic CRUD methods are inherited
    // Custom query methods can be added here if needed
}
