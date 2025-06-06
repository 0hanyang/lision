package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "refrigerators")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Refrigerator extends StorageContainer {

    private String model;
    private Double temperature;
    private Integer capacity; // e.g., number of layers or total box capacity
    private Integer remainingCapacity;

    @OneToMany(mappedBy = "refrigerator", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<FreezerLayer> layers = new ArrayList<>();

    public Refrigerator(String containerId, String description, String model, Double temperature, Integer capacity) {
        super(containerId, description);
        this.model = model;
        this.temperature = temperature;
        this.capacity = capacity;
        this.remainingCapacity = capacity; // Initially, remaining capacity is full capacity
    }

    // Helper methods to manage layers if needed
    public void addLayer(FreezerLayer layer) {
        layers.add(layer);
        layer.setRefrigerator(this);
    }

    public void removeLayer(FreezerLayer layer) {
        layers.remove(layer);
        layer.setRefrigerator(null);
    }
}
