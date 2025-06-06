package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "box_grids")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class BoxGrid extends StorageContainer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_box_id", nullable = false)
    @ToString.Exclude
    private SampleBox sampleBox;

    @OneToOne(mappedBy = "boxGrid", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Sample sample;

    // containerId will be like "G1-1", "G8-3"
    public BoxGrid(String containerId, String description, SampleBox sampleBox) {
        super(containerId, description);
        this.sampleBox = sampleBox;
    }

    public void setSample(Sample sample) {
        if (sample == null) {
            if (this.sample != null) {
                this.sample.setBoxGrid(null);
            }
        } else {
            sample.setBoxGrid(this);
        }
        this.sample = sample;
    }
}
