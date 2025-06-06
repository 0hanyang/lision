package com.example.biobanksystem.service;

import com.example.biobanksystem.model.FreezerLayer;
import com.example.biobanksystem.model.Refrigerator;
import com.example.biobanksystem.repository.BoxGridRepository;
import com.example.biobanksystem.repository.DrawerRepository;
import com.example.biobanksystem.repository.FreezerLayerRepository;
import com.example.biobanksystem.repository.RefrigeratorRepository;
import com.example.biobanksystem.repository.SampleBoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefrigeratorServiceTest {

    @Mock
    private RefrigeratorRepository refrigeratorRepository;
    @Mock
    private FreezerLayerRepository freezerLayerRepository;
    @Mock
    private DrawerRepository drawerRepository; // Added, though not used in these specific tests yet
    @Mock
    private SampleBoxRepository sampleBoxRepository; // Added
    @Mock
    private BoxGridRepository boxGridRepository; // Added
    @Mock
    private ContainerCodeService containerCodeService;

    @InjectMocks
    private RefrigeratorService refrigeratorService;

    private Refrigerator refrigerator;
    private FreezerLayer freezerLayer;

    @BeforeEach
    void setUp() {
        refrigerator = new Refrigerator("R01", "Test Fridge", "ModelX", -80.0, 100);
        refrigerator.setId(1L);

        freezerLayer = new FreezerLayer("L1", "Layer 1", refrigerator);
        freezerLayer.setId(10L);
    }

    @Test
    void createRefrigerator_validCode_shouldSaveRefrigerator() {
        when(containerCodeService.isValidRefrigeratorCode("R01")).thenReturn(true);
        when(refrigeratorRepository.save(any(Refrigerator.class))).thenReturn(refrigerator);

        Refrigerator saved = refrigeratorService.createRefrigerator(refrigerator);

        assertNotNull(saved);
        assertEquals("R01", saved.getContainerId());
        verify(refrigeratorRepository, times(1)).save(refrigerator);
    }

    @Test
    void createRefrigerator_invalidCode_shouldThrowException() {
        when(containerCodeService.isValidRefrigeratorCode("INVALID")).thenReturn(false);
        Refrigerator invalidRefrigerator = new Refrigerator("INVALID", "Test", "ModelX", -80.0, 100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.createRefrigerator(invalidRefrigerator);
        });
        assertEquals("Invalid Refrigerator code format: INVALID", exception.getMessage());
        verify(refrigeratorRepository, never()).save(any(Refrigerator.class));
    }

    @Test
    void createRefrigerator_nullRefrigerator_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.createRefrigerator(null);
        });
        assertEquals("Refrigerator and its containerId must not be null.", exception.getMessage());
         verify(refrigeratorRepository, never()).save(any(Refrigerator.class));
    }

    @Test
    void createRefrigerator_nullContainerId_shouldThrowException() {
        refrigerator.setContainerId(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.createRefrigerator(refrigerator);
        });
        assertEquals("Refrigerator and its containerId must not be null.", exception.getMessage());
        verify(refrigeratorRepository, never()).save(any(Refrigerator.class));
    }


    @Test
    void getRefrigeratorById_exists_shouldReturnRefrigerator() {
        when(refrigeratorRepository.findById(1L)).thenReturn(Optional.of(refrigerator));
        Optional<Refrigerator> found = refrigeratorService.getRefrigeratorById(1L);
        assertTrue(found.isPresent());
        assertEquals(refrigerator.getId(), found.get().getId());
    }

    @Test
    void getRefrigeratorById_notFound_shouldReturnEmptyOptional() {
        when(refrigeratorRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Refrigerator> found = refrigeratorService.getRefrigeratorById(2L);
        assertFalse(found.isPresent());
    }

    @Test
    void getAllRefrigerators_shouldReturnList() {
        List<Refrigerator> fridges = new ArrayList<>();
        fridges.add(refrigerator);
        when(refrigeratorRepository.findAll()).thenReturn(fridges);

        List<Refrigerator> result = refrigeratorService.getAllRefrigerators();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("R01", result.get(0).getContainerId());
    }


    @Test
    void addFreezerLayer_valid_shouldSaveFreezerLayer() {
        when(refrigeratorRepository.findById(1L)).thenReturn(Optional.of(refrigerator));
        when(containerCodeService.isValidFreezerLayerCode("L1")).thenReturn(true);
        when(freezerLayerRepository.save(any(FreezerLayer.class))).thenReturn(freezerLayer);

        FreezerLayer savedLayer = refrigeratorService.addFreezerLayer(1L, freezerLayer);

        assertNotNull(savedLayer);
        assertEquals("L1", savedLayer.getContainerId());
        assertEquals(refrigerator, savedLayer.getRefrigerator());
        verify(freezerLayerRepository, times(1)).save(freezerLayer);
    }

    @Test
    void addFreezerLayer_invalidCode_shouldThrowException() {
        FreezerLayer layerWithInvalidCode = new FreezerLayer();
        layerWithInvalidCode.setContainerId("INVALID-L");
        lenient().when(containerCodeService.isValidFreezerLayerCode("INVALID-L")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.addFreezerLayer(1L, layerWithInvalidCode);
        });
        assertEquals("Invalid FreezerLayer code format: INVALID-L", exception.getMessage());
        verify(freezerLayerRepository, never()).save(any(FreezerLayer.class));
    }

    @Test
    void addFreezerLayer_refrigeratorNotFound_shouldThrowException() {
        FreezerLayer layerForThisTest = new FreezerLayer();
        layerForThisTest.setContainerId("L1"); // Explicitly set ID for this test instance

        // Ensure this mock is specific and should be hit
        when(containerCodeService.isValidFreezerLayerCode(eq("L1"))).thenReturn(true);
        when(refrigeratorRepository.findById(eq(2L))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.addFreezerLayer(2L, layerForThisTest);
        });
        assertEquals("Refrigerator with id 2 not found.", exception.getMessage());
        verify(freezerLayerRepository, never()).save(any(FreezerLayer.class));
    }

    @Test
    void addFreezerLayer_duplicateCodeInSameRefrigerator_shouldThrowException() {
        Refrigerator existingFridge = new Refrigerator("R01", "Test Fridge", "ModelX", -80.0, 100);
        existingFridge.setId(1L);
        FreezerLayer existingLayer = new FreezerLayer("L1", "Existing Layer 1", existingFridge);
        existingFridge.getLayers().add(existingLayer); // Simulate existing layer

        when(refrigeratorRepository.findById(1L)).thenReturn(Optional.of(existingFridge));
        when(containerCodeService.isValidFreezerLayerCode("L1")).thenReturn(true);

        FreezerLayer newLayerWithSameCode = new FreezerLayer("L1", "New Layer 1", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.addFreezerLayer(1L, newLayerWithSameCode);
        });
        assertEquals("FreezerLayer code L1 already exists in this refrigerator.", exception.getMessage());
        verify(freezerLayerRepository, never()).save(any(FreezerLayer.class));
    }


    @Test
    void getFreezerLayersByRefrigeratorId_exists_shouldReturnLayers() {
        refrigerator.getLayers().add(freezerLayer); // Add layer to the refrigerator
        when(refrigeratorRepository.findById(1L)).thenReturn(Optional.of(refrigerator));

        List<FreezerLayer> layers = refrigeratorService.getFreezerLayersByRefrigeratorId(1L);

        assertNotNull(layers);
        assertFalse(layers.isEmpty());
        assertEquals(1, layers.size());
        assertEquals("L1", layers.get(0).getContainerId());
    }

    @Test
    void getFreezerLayersByRefrigeratorId_refrigeratorNotFound_shouldThrowException() {
        when(refrigeratorRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            refrigeratorService.getFreezerLayersByRefrigeratorId(2L);
        });
        assertEquals("Refrigerator with id 2 not found.", exception.getMessage());
    }

    // TODO: Add tests for updateRefrigerator, deleteRefrigerator
    // TODO: Add tests for addDrawer, addSampleBox, addBoxGrid (and their get methods if created separately)
}
