package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "samples")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String sampleCode; // e.g., "P03058F-1"

    @ManyToOne(fetch = FetchType.EAGER) // SampleType is likely to be needed whenever Sample is loaded
    @JoinColumn(name = "sample_type_id", nullable = false)
    private SampleType sampleType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_grid_id", unique = true) // Each sample can only be in one grid location
    @ToString.Exclude
    private BoxGrid boxGrid;

    private LocalDate collectionDate;

    @Lob // For potentially long notes
    private String notes;

    public Sample(String sampleCode, SampleType sampleType, LocalDate collectionDate, String notes) {
        this.sampleCode = sampleCode;
        this.sampleType = sampleType;
        this.collectionDate = collectionDate;
        this.notes = notes;
    }

    public void setBoxGrid(BoxGrid boxGrid) {
        // This logic ensures bidirectional consistency and handles removing sample from a grid
        if (this.boxGrid != null && this.boxGrid.equals(boxGrid)) {
            return; // No change needed
        }

        // If this sample was previously in another grid, clear that association
        if (this.boxGrid != null) {
            this.boxGrid.setSample(null);
        }

        this.boxGrid = boxGrid;

        // If the new grid is not null and doesn't already have this sample, set it
        if (boxGrid != null && (boxGrid.getSample() == null || !boxGrid.getSample().equals(this))) {
            boxGrid.setSample(this);
        }
    }
}
