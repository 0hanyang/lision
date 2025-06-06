package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "freezer_layers")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class FreezerLayer extends StorageContainer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigerator_id", nullable = false)
    @ToString.Exclude
    private Refrigerator refrigerator;

    @OneToMany(mappedBy = "layer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Drawer> drawers = new ArrayList<>();

    // containerId will be like "L1", "L2"
    public FreezerLayer(String containerId, String description, Refrigerator refrigerator) {
        super(containerId, description);
        this.refrigerator = refrigerator;
    }

    // Helper methods to manage drawers
    public void addDrawer(Drawer drawer) {
        drawers.add(drawer);
        drawer.setLayer(this);
    }

    public void removeDrawer(Drawer drawer) {
        drawers.remove(drawer);
        drawer.setLayer(null);
    }
}
