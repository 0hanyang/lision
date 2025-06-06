package com.example.biobanksystem.controller;

import com.example.biobanksystem.model.FreezerLayer;
import com.example.biobanksystem.model.Refrigerator;
import com.example.biobanksystem.service.RefrigeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refrigerators")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    @Autowired
    public RefrigeratorController(RefrigeratorService refrigeratorService) {
        this.refrigeratorService = refrigeratorService;
    }

    // Create Refrigerator
    @PostMapping("/")
    public ResponseEntity<Refrigerator> createRefrigerator(@RequestBody Refrigerator refrigerator) {
        Refrigerator createdRefrigerator = refrigeratorService.createRefrigerator(refrigerator);
        return new ResponseEntity<>(createdRefrigerator, HttpStatus.CREATED);
    }

    // Read (Single) Refrigerator
    @GetMapping("/{refrigeratorId}")
    public ResponseEntity<Refrigerator> getRefrigeratorById(@PathVariable Long refrigeratorId) {
        return refrigeratorService.getRefrigeratorById(refrigeratorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read (All) Refrigerators
    @GetMapping("/")
    public ResponseEntity<List<Refrigerator>> getAllRefrigerators() {
        List<Refrigerator> refrigerators = refrigeratorService.getAllRefrigerators();
        return ResponseEntity.ok(refrigerators);
    }

    // Update Refrigerator
    @PutMapping("/{refrigeratorId}")
    public ResponseEntity<Refrigerator> updateRefrigerator(@PathVariable Long refrigeratorId, @RequestBody Refrigerator refrigeratorDetails) {
        Refrigerator updatedRefrigerator = refrigeratorService.updateRefrigerator(refrigeratorId, refrigeratorDetails);
        return ResponseEntity.ok(updatedRefrigerator);
    }

    // Delete Refrigerator
    @DeleteMapping("/{refrigeratorId}")
    public ResponseEntity<Void> deleteRefrigerator(@PathVariable Long refrigeratorId) {
        refrigeratorService.deleteRefrigerator(refrigeratorId);
        return ResponseEntity.noContent().build();
    }

    // Get Layers for a Refrigerator
    @GetMapping("/{refrigeratorId}/layers")
    public ResponseEntity<List<FreezerLayer>> getFreezerLayersByRefrigeratorId(@PathVariable Long refrigeratorId) {
        List<FreezerLayer> layers = refrigeratorService.getFreezerLayersByRefrigeratorId(refrigeratorId);
        return ResponseEntity.ok(layers);
    }
}
