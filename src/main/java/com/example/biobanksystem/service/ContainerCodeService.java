package com.example.biobanksystem.service;

import java.util.List;

public interface ContainerCodeService {

    boolean isValidRefrigeratorCode(String code);

    boolean isValidFreezerLayerCode(String code);

    boolean isValidDrawerCode(String code);

    boolean isValidSampleBoxCode(String code);

    boolean isValidBoxGridCode(String code);

    // Generation methods (optional for now)
    // String generateNextRefrigeratorCode(List<String> existingCodes);
}
