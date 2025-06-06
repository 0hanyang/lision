package com.example.biobanksystem.repository;

import com.example.biobanksystem.model.Drawer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawerRepository extends JpaRepository<Drawer, Long> {
    // Basic CRUD methods are inherited
    // For example: List<Drawer> findByLayerId(Long freezerLayerId);
}
