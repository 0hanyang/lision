package com.example.biobanksystem.service;

import com.example.biobanksystem.model.BoxGrid;
import com.example.biobanksystem.model.SampleBox;
import com.example.biobanksystem.repository.SampleBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SampleBoxService {

    private final SampleBoxRepository sampleBoxRepository;

    @Autowired
    public SampleBoxService(SampleBoxRepository sampleBoxRepository) {
        this.sampleBoxRepository = sampleBoxRepository;
    }

    @Transactional(readOnly = true)
    public List<BoxGrid> getBoxGridsBySampleBoxId(Long sampleBoxId) {
        SampleBox sampleBox = sampleBoxRepository.findById(sampleBoxId)
            .orElseThrow(() -> new IllegalArgumentException("SampleBox with id " + sampleBoxId + " not found."));
        return sampleBox.getGrids();
    }
}
