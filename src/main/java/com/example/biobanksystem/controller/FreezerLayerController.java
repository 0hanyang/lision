package com.example.biobanksystem.controller;

import com.example.biobanksystem.model.Drawer;
import com.example.biobanksystem.service.FreezerLayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/freezerlayers")
public class FreezerLayerController {

    private final FreezerLayerService freezerLayerService;

    @Autowired
    public FreezerLayerController(FreezerLayerService freezerLayerService) {
        this.freezerLayerService = freezerLayerService;
    }

    // Get Drawers for a Layer
    @GetMapping("/{layerId}/drawers")
    public ResponseEntity<List<Drawer>> getDrawersByFreezerLayerId(@PathVariable Long layerId) {
        List<Drawer> drawers = freezerLayerService.getDrawersByFreezerLayerId(layerId);
        return ResponseEntity.ok(drawers);
    }
}
