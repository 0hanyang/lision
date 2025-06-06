package com.example.biobanksystem.controller;

import com.example.biobanksystem.model.BoxGrid;
import com.example.biobanksystem.service.SampleBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sampleboxes")
public class SampleBoxController {

    private final SampleBoxService sampleBoxService;

    @Autowired
    public SampleBoxController(SampleBoxService sampleBoxService) {
        this.sampleBoxService = sampleBoxService;
    }

    // Get Grids for a Box
    @GetMapping("/{boxId}/grids")
    public ResponseEntity<List<BoxGrid>> getBoxGridsBySampleBoxId(@PathVariable Long boxId) {
        List<BoxGrid> grids = sampleBoxService.getBoxGridsBySampleBoxId(boxId);
        return ResponseEntity.ok(grids);
    }
}
