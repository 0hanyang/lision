package com.example.biobanksystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContainerCodeServiceImplTest {

    private ContainerCodeService containerCodeService;

    @BeforeEach
    void setUp() {
        containerCodeService = new ContainerCodeServiceImpl();
    }

    @Test
    void isValidRefrigeratorCode_validCodes() {
        assertTrue(containerCodeService.isValidRefrigeratorCode("A01"));
        assertTrue(containerCodeService.isValidRefrigeratorCode("Z99"));
        assertTrue(containerCodeService.isValidRefrigeratorCode("M50"));
    }

    @Test
    void isValidRefrigeratorCode_invalidCodes() {
        assertFalse(containerCodeService.isValidRefrigeratorCode("123"));      // No letter
        assertFalse(containerCodeService.isValidRefrigeratorCode("AA1"));      // Too many letters
        assertFalse(containerCodeService.isValidRefrigeratorCode("A123"));     // Too many digits
        assertFalse(containerCodeService.isValidRefrigeratorCode("A0"));       // Too few digits
        assertFalse(containerCodeService.isValidRefrigeratorCode("a01"));      // Lowercase letter
        assertFalse(containerCodeService.isValidRefrigeratorCode("A 01"));     // Space
        assertFalse(containerCodeService.isValidRefrigeratorCode(""));         // Empty
        assertFalse(containerCodeService.isValidRefrigeratorCode(null));       // Null
    }

    @Test
    void isValidFreezerLayerCode_validCodes() {
        assertTrue(containerCodeService.isValidFreezerLayerCode("L1"));
        assertTrue(containerCodeService.isValidFreezerLayerCode("L2"));
        assertTrue(containerCodeService.isValidFreezerLayerCode("L3"));
        assertTrue(containerCodeService.isValidFreezerLayerCode("L4"));
    }

    @Test
    void isValidFreezerLayerCode_invalidCodes() {
        assertFalse(containerCodeService.isValidFreezerLayerCode("L0"));   // Digit out of range
        assertFalse(containerCodeService.isValidFreezerLayerCode("L5"));   // Digit out of range
        assertFalse(containerCodeService.isValidFreezerLayerCode("LL1"));  // Too many letters
        assertFalse(containerCodeService.isValidFreezerLayerCode("L"));    // Missing digit
        assertFalse(containerCodeService.isValidFreezerLayerCode("l1"));   // Lowercase letter
        assertFalse(containerCodeService.isValidFreezerLayerCode("L 1"));  // Space
        assertFalse(containerCodeService.isValidFreezerLayerCode(""));
        assertFalse(containerCodeService.isValidFreezerLayerCode(null));
    }

    @Test
    void isValidDrawerCode_validCodes() {
        assertTrue(containerCodeService.isValidDrawerCode("F1-1"));
        assertTrue(containerCodeService.isValidDrawerCode("F9-9"));
        assertTrue(containerCodeService.isValidDrawerCode("F0-0"));
    }

    @Test
    void isValidDrawerCode_invalidCodes() {
        assertFalse(containerCodeService.isValidDrawerCode("FF1-1")); // Too many letters
        assertFalse(containerCodeService.isValidDrawerCode("F1"));    // Missing dash and second digit
        assertFalse(containerCodeService.isValidDrawerCode("F-1"));   // Missing first digit
        assertFalse(containerCodeService.isValidDrawerCode("F1-"));   // Missing second digit
        assertFalse(containerCodeService.isValidDrawerCode("f1-1"));  // Lowercase letter
        assertFalse(containerCodeService.isValidDrawerCode("F1 - 1"));// Space
        assertFalse(containerCodeService.isValidDrawerCode("F1-12")); // Too many digits after dash
        assertFalse(containerCodeService.isValidDrawerCode("F12-1")); // Too many digits before dash
        assertFalse(containerCodeService.isValidDrawerCode(""));
        assertFalse(containerCodeService.isValidDrawerCode(null));
    }

    @Test
    void isValidSampleBoxCode_validCodes() {
        assertTrue(containerCodeService.isValidSampleBoxCode("B1"));
        assertTrue(containerCodeService.isValidSampleBoxCode("B3"));
        assertTrue(containerCodeService.isValidSampleBoxCode("B5"));
    }

    @Test
    void isValidSampleBoxCode_invalidCodes() {
        assertFalse(containerCodeService.isValidSampleBoxCode("B0"));  // Digit out of range
        assertFalse(containerCodeService.isValidSampleBoxCode("B6"));  // Digit out of range
        assertFalse(containerCodeService.isValidSampleBoxCode("BB1")); // Too many letters
        assertFalse(containerCodeService.isValidSampleBoxCode("B"));   // Missing digit
        assertFalse(containerCodeService.isValidSampleBoxCode("b1"));  // Lowercase letter
        assertFalse(containerCodeService.isValidSampleBoxCode("B 1")); // Space
        assertFalse(containerCodeService.isValidSampleBoxCode(""));
        assertFalse(containerCodeService.isValidSampleBoxCode(null));
    }

    @Test
    void isValidBoxGridCode_validCodes() {
        assertTrue(containerCodeService.isValidBoxGridCode("G1-1"));
        assertTrue(containerCodeService.isValidBoxGridCode("G8-3"));
        assertTrue(containerCodeService.isValidBoxGridCode("G9-9"));
        assertTrue(containerCodeService.isValidBoxGridCode("G0-0"));
    }

    @Test
    void isValidBoxGridCode_invalidCodes() {
        assertFalse(containerCodeService.isValidBoxGridCode("GG1-1"));// Too many letters
        assertFalse(containerCodeService.isValidBoxGridCode("G1"));   // Missing dash and second digit
        assertFalse(containerCodeService.isValidBoxGridCode("G-1"));  // Missing first digit
        assertFalse(containerCodeService.isValidBoxGridCode("G1-"));  // Missing second digit
        assertFalse(containerCodeService.isValidBoxGridCode("g1-1")); // Lowercase letter
        assertFalse(containerCodeService.isValidBoxGridCode("G1 - 1"));// Space
        assertFalse(containerCodeService.isValidBoxGridCode("G1-12"));// Too many digits after dash
        assertFalse(containerCodeService.isValidBoxGridCode("G12-1"));// Too many digits before dash
        assertFalse(containerCodeService.isValidBoxGridCode(""));
        assertFalse(containerCodeService.isValidBoxGridCode(null));
    }
}
