package com.example.biobanksystem.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ContainerCodeServiceImpl implements ContainerCodeService {

    // A-Z followed by 2 digits (e.g., A01)
    private static final Pattern REFRIGERATOR_CODE_PATTERN = Pattern.compile("^[A-Z]\\d{2}$");

    // L followed by 1 digit (1-4) (e.g., L1, L2, L3, L4)
    private static final Pattern FREEZER_LAYER_CODE_PATTERN = Pattern.compile("^L[1-4]$");

    // F followed by a digit, then '-', then a digit (e.g., F1-1, F3-1, F9-9)
    private static final Pattern DRAWER_CODE_PATTERN = Pattern.compile("^F\\d-\\d$");

    // B followed by 1 digit (1-5) (e.g., B1, B2, B3, B4, B5)
    private static final Pattern SAMPLE_BOX_CODE_PATTERN = Pattern.compile("^B[1-5]$");

    // G followed by a digit, then '-', then a digit (e.g., G1-1, G8-3, G9-9)
    private static final Pattern BOX_GRID_CODE_PATTERN = Pattern.compile("^G\\d-\\d$");

    @Override
    public boolean isValidRefrigeratorCode(String code) {
        return code != null && REFRIGERATOR_CODE_PATTERN.matcher(code).matches();
    }

    @Override
    public boolean isValidFreezerLayerCode(String code) {
        return code != null && FREEZER_LAYER_CODE_PATTERN.matcher(code).matches();
    }

    @Override
    public boolean isValidDrawerCode(String code) {
        return code != null && DRAWER_CODE_PATTERN.matcher(code).matches();
    }

    @Override
    public boolean isValidSampleBoxCode(String code) {
        return code != null && SAMPLE_BOX_CODE_PATTERN.matcher(code).matches();
    }

    @Override
    public boolean isValidBoxGridCode(String code) {
        return code != null && BOX_GRID_CODE_PATTERN.matcher(code).matches();
    }
}
