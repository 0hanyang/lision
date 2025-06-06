package com.example.biobanksystem.service;

import com.example.biobanksystem.model.*; // Import all models
import com.example.biobanksystem.repository.*; // Import all repositories
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Added import
import java.util.Optional;

@Service
public class RefrigeratorService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final FreezerLayerRepository freezerLayerRepository;
    private final DrawerRepository drawerRepository;
    private final SampleBoxRepository sampleBoxRepository;
    private final BoxGridRepository boxGridRepository;
    private final ContainerCodeService containerCodeService;

    @Autowired
    public RefrigeratorService(RefrigeratorRepository refrigeratorRepository,
                               FreezerLayerRepository freezerLayerRepository,
                               DrawerRepository drawerRepository,
                               SampleBoxRepository sampleBoxRepository,
                               BoxGridRepository boxGridRepository,
                               ContainerCodeService containerCodeService) {
        this.refrigeratorRepository = refrigeratorRepository;
        this.freezerLayerRepository = freezerLayerRepository;
        this.drawerRepository = drawerRepository;
        this.sampleBoxRepository = sampleBoxRepository;
        this.boxGridRepository = boxGridRepository;
        this.containerCodeService = containerCodeService;
    }

    @Transactional
    public Refrigerator createRefrigerator(Refrigerator refrigerator) {
        if (refrigerator == null || refrigerator.getContainerId() == null) {
            throw new IllegalArgumentException("Refrigerator and its containerId must not be null.");
        }
        if (!containerCodeService.isValidRefrigeratorCode(refrigerator.getContainerId())) {
            throw new IllegalArgumentException("Invalid Refrigerator code format: " + refrigerator.getContainerId());
        }
        // Optional: Check if containerId already exists
        // refrigeratorRepository.findByContainerId(refrigerator.getContainerId()).ifPresent(r -> {
        //    throw new IllegalArgumentException("Refrigerator code " + r.getContainerId() + " already exists.");
        // });
        return refrigeratorRepository.save(refrigerator);
    }

    @Transactional(readOnly = true)
    public Optional<Refrigerator> getRefrigeratorById(Long id) {
        return refrigeratorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Refrigerator> getAllRefrigerators() {
        return refrigeratorRepository.findAll();
    }

    @Transactional
    public Refrigerator updateRefrigerator(Long id, Refrigerator refrigeratorDetails) {
        Refrigerator refrigerator = refrigeratorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refrigerator with id " + id + " not found."));

        if (refrigeratorDetails.getContainerId() != null) {
            if (!containerCodeService.isValidRefrigeratorCode(refrigeratorDetails.getContainerId())) {
                throw new IllegalArgumentException("Invalid Refrigerator code format: " + refrigeratorDetails.getContainerId());
            }
            // Optional: Check if new containerId already exists and is not the current one
            refrigerator.setContainerId(refrigeratorDetails.getContainerId());
        }
        if (refrigeratorDetails.getDescription() != null) {
            refrigerator.setDescription(refrigeratorDetails.getDescription());
        }
        if (refrigeratorDetails.getModel() != null) {
            refrigerator.setModel(refrigeratorDetails.getModel());
        }
        if (refrigeratorDetails.getTemperature() != null) {
            refrigerator.setTemperature(refrigeratorDetails.getTemperature());
        }
        if (refrigeratorDetails.getCapacity() != null) {
            refrigerator.setCapacity(refrigeratorDetails.getCapacity());
            // Consider logic for remainingCapacity if capacity changes
        }
        // Note: managing remainingCapacity might need more complex logic, especially if layers are involved.
        // For now, not updating remainingCapacity directly here unless explicitly provided.
        if (refrigeratorDetails.getRemainingCapacity() != null) {
            refrigerator.setRemainingCapacity(refrigeratorDetails.getRemainingCapacity());
        }

        return refrigeratorRepository.save(refrigerator);
    }

    @Transactional
    public void deleteRefrigerator(Long id) {
        if (!refrigeratorRepository.existsById(id)) {
            throw new IllegalArgumentException("Refrigerator with id " + id + " not found.");
        }
        refrigeratorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FreezerLayer> getFreezerLayersByRefrigeratorId(Long refrigeratorId) {
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId)
            .orElseThrow(() -> new IllegalArgumentException("Refrigerator with id " + refrigeratorId + " not found."));
        return refrigerator.getLayers();
    }


    @Transactional
    public FreezerLayer addFreezerLayer(Long refrigeratorId, FreezerLayer layer) {
        if (layer == null || layer.getContainerId() == null) {
            throw new IllegalArgumentException("FreezerLayer and its containerId must not be null.");
        }

        if (!containerCodeService.isValidFreezerLayerCode(layer.getContainerId())) {
            throw new IllegalArgumentException("Invalid FreezerLayer code format: " + layer.getContainerId());
        }

        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId)
                .orElseThrow(() -> new IllegalArgumentException("Refrigerator with id " + refrigeratorId + " not found."));

        // Check for duplicate containerId within the same refrigerator
        boolean codeExistsInRefrigerator = refrigerator.getLayers().stream()
                .anyMatch(existingLayer -> existingLayer.getContainerId().equals(layer.getContainerId()));
        if (codeExistsInRefrigerator) {
            throw new IllegalArgumentException("FreezerLayer code " + layer.getContainerId() + " already exists in this refrigerator.");
        }

        layer.setRefrigerator(refrigerator);
        return freezerLayerRepository.save(layer);
    }

    @Transactional
    public Drawer addDrawer(Long freezerLayerId, Drawer drawer) {
        if (drawer == null || drawer.getContainerId() == null) {
            throw new IllegalArgumentException("Drawer and its containerId must not be null.");
        }
        if (!containerCodeService.isValidDrawerCode(drawer.getContainerId())) {
            throw new IllegalArgumentException("Invalid Drawer code format: " + drawer.getContainerId());
        }

        FreezerLayer freezerLayer = freezerLayerRepository.findById(freezerLayerId)
                .orElseThrow(() -> new IllegalArgumentException("FreezerLayer with id " + freezerLayerId + " not found."));

        boolean codeExistsInLayer = freezerLayer.getDrawers().stream()
                .anyMatch(existingDrawer -> existingDrawer.getContainerId().equals(drawer.getContainerId()));
        if (codeExistsInLayer) {
            throw new IllegalArgumentException("Drawer code " + drawer.getContainerId() + " already exists in this freezer layer.");
        }

        drawer.setLayer(freezerLayer);
        return drawerRepository.save(drawer);
    }

    @Transactional
    public SampleBox addSampleBox(Long drawerId, SampleBox sampleBox) {
        if (sampleBox == null || sampleBox.getContainerId() == null) {
            throw new IllegalArgumentException("SampleBox and its containerId must not be null.");
        }
        if (!containerCodeService.isValidSampleBoxCode(sampleBox.getContainerId())) {
            throw new IllegalArgumentException("Invalid SampleBox code format: " + sampleBox.getContainerId());
        }

        Drawer drawer = drawerRepository.findById(drawerId)
                .orElseThrow(() -> new IllegalArgumentException("Drawer with id " + drawerId + " not found."));

        boolean codeExistsInDrawer = drawer.getBoxes().stream()
                .anyMatch(existingBox -> existingBox.getContainerId().equals(sampleBox.getContainerId()));
        if (codeExistsInDrawer) {
            throw new IllegalArgumentException("SampleBox code " + sampleBox.getContainerId() + " already exists in this drawer.");
        }

        sampleBox.setDrawer(drawer);
        return sampleBoxRepository.save(sampleBox);
    }

    @Transactional
    public BoxGrid addBoxGrid(Long sampleBoxId, BoxGrid boxGrid) {
        if (boxGrid == null || boxGrid.getContainerId() == null) {
            throw new IllegalArgumentException("BoxGrid and its containerId must not be null.");
        }
        if (!containerCodeService.isValidBoxGridCode(boxGrid.getContainerId())) {
            throw new IllegalArgumentException("Invalid BoxGrid code format: " + boxGrid.getContainerId());
        }

        SampleBox sampleBox = sampleBoxRepository.findById(sampleBoxId)
                .orElseThrow(() -> new IllegalArgumentException("SampleBox with id " + sampleBoxId + " not found."));

        boolean codeExistsInSampleBox = sampleBox.getGrids().stream()
                .anyMatch(existingGrid -> existingGrid.getContainerId().equals(boxGrid.getContainerId()));
        if (codeExistsInSampleBox) {
            throw new IllegalArgumentException("BoxGrid code " + boxGrid.getContainerId() + " already exists in this sample box.");
        }

        boxGrid.setSampleBox(sampleBox);
        return boxGridRepository.save(boxGrid);
    }
}
