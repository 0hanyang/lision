package com.example.biobanksystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sample_types")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SampleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Serum", "Plasma"

    private String colorCode;

    private String boxSymbol;

    public SampleType(String name, String colorCode, String boxSymbol) {
        this.name = name;
        this.colorCode = colorCode;
        this.boxSymbol = boxSymbol;
    }
}
