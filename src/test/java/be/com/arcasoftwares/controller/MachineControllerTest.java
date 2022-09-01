package be.com.arcasoftwares.controller;

import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.service.MachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MachineControllerTest {

    private MachineService mockMachineService;
    private MachineController machineController;

    @BeforeEach
    void setup() {
        mockMachineService = mock(MachineService.class);
        machineController = new MachineController(mockMachineService);
    }

    @Test
    void persistShowSuccessFullyPersist() {
        MachineModel model = new MachineModel("machine01", "machine-01");
        when(mockMachineService.save(any())).thenReturn(model);
        MachineModel persist = machineController.persist(model);

        assertEquals("machine01", persist.getKey());
        assertEquals("machine-01", persist.getName());
    }

    @Test
    void getAllShouldReturnAllTheMachines() {
        MachineModel model01 = new MachineModel("machine01", "machine-01");
        MachineModel model02 = new MachineModel("machine02", "machine-02");
        when(mockMachineService.getAll()).thenReturn(List.of(model01, model02));

        List<MachineModel> all = machineController.getAll();

        assertEquals(2, all.size());
    }
}