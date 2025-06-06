package com.example.biobanksystem.service;

import com.example.biobanksystem.model.Drawer;
import com.example.biobanksystem.model.FreezerLayer;
import com.example.biobanksystem.repository.FreezerLayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FreezerLayerService {

    private final FreezerLayerRepository freezerLayerRepository;
    // ContainerCodeService might be needed for adding drawers, etc., later
    // private final ContainerCodeService containerCodeService;

    @Autowired
    public FreezerLayerService(FreezerLayerRepository freezerLayerRepository) {
        this.freezerLayerRepository = freezerLayerRepository;
    }

    @Transactional(readOnly = true)
    public List<Drawer> getDrawersByFreezerLayerId(Long freezerLayerId) {
        FreezerLayer freezerLayer = freezerLayerRepository.findById(freezerLayerId)
            .orElseThrow(() -> new IllegalArgumentException("FreezerLayer with id " + freezerLayerId + " not found."));
        return freezerLayer.getDrawers();
    }

    // Other methods like addDrawer to FreezerLayer could be here or in RefrigeratorService
    // (currently in RefrigeratorService for simplicity as per previous steps)
}
