package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sample_boxes")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class SampleBox extends StorageContainer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawer_id", nullable = false)
    @ToString.Exclude
    private Drawer drawer;

    @OneToMany(mappedBy = "sampleBox", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<BoxGrid> grids = new ArrayList<>();

    // containerId will be like "B1", "B2"
    public SampleBox(String containerId, String description, Drawer drawer) {
        super(containerId, description);
        this.drawer = drawer;
    }

    // Helper methods to manage grids
    public void addBoxGrid(BoxGrid boxGrid) {
        grids.add(boxGrid);
        boxGrid.setSampleBox(this);
    }

    public void removeBoxGrid(BoxGrid boxGrid) {
        grids.remove(boxGrid);
        boxGrid.setSampleBox(null);
    }
}
