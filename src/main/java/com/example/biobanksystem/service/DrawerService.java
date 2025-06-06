package com.example.biobanksystem.service;

import com.example.biobanksystem.model.Drawer;
import com.example.biobanksystem.model.SampleBox;
import com.example.biobanksystem.repository.DrawerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DrawerService {

    private final DrawerRepository drawerRepository;

    @Autowired
    public DrawerService(DrawerRepository drawerRepository) {
        this.drawerRepository = drawerRepository;
    }

    @Transactional(readOnly = true)
    public List<SampleBox> getSampleBoxesByDrawerId(Long drawerId) {
        Drawer drawer = drawerRepository.findById(drawerId)
            .orElseThrow(() -> new IllegalArgumentException("Drawer with id " + drawerId + " not found."));
        return drawer.getBoxes();
    }
}
