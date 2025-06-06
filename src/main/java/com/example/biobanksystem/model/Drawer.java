package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drawers")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Drawer extends StorageContainer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layer_id", nullable = false)
    @ToString.Exclude
    private FreezerLayer layer;

    @OneToMany(mappedBy = "drawer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SampleBox> boxes = new ArrayList<>();

    // containerId will be like "F1-1", "F1-2"
    public Drawer(String containerId, String description, FreezerLayer layer) {
        super(containerId, description);
        this.layer = layer;
    }

    // Helper methods to manage boxes
    public void addSampleBox(SampleBox sampleBox) {
        boxes.add(sampleBox);
        sampleBox.setDrawer(this);
    }

    public void removeSampleBox(SampleBox sampleBox) {
        boxes.remove(sampleBox);
        sampleBox.setDrawer(null);
    }
}
