package com.example.biobanksystem.controller;

import com.example.biobanksystem.model.SampleBox;
import com.example.biobanksystem.service.DrawerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drawers")
public class DrawerController {

    private final DrawerService drawerService;

    @Autowired
    public DrawerController(DrawerService drawerService) {
        this.drawerService = drawerService;
    }

    // Get Boxes for a Drawer
    @GetMapping("/{drawerId}/boxes")
    public ResponseEntity<List<SampleBox>> getSampleBoxesByDrawerId(@PathVariable Long drawerId) {
        List<SampleBox> boxes = drawerService.getSampleBoxesByDrawerId(drawerId);
        return ResponseEntity.ok(boxes);
    }
}
