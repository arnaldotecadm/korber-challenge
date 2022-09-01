package be.com.arcasoftwares.service;

import be.com.arcasoftwares.exception.CsvParseException;
import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.repository.MachineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MachineServiceTest {

    MachineRepository mockMachineRepository;
    MachineService machineService;

    @BeforeEach
    void setup() {
        mockMachineRepository = mock(MachineRepository.class);
        machineService = new MachineService(mockMachineRepository);
    }

    @Test
    void saveShouldSaveTheDataSuccessfully() {
        MachineModel machineModel = getMachineModel("key01", "name01");
        when(mockMachineRepository.upsert(any())).thenReturn(machineModel);
        MachineModel model = machineService.save(machineModel);

        assertEquals("key01", model.getKey());
        assertEquals("name01", model.getName());
    }

    @Test
    void getAllShouldRetrieveAllValues() {
        MachineModel machineModel01 = getMachineModel("key01", "name01");
        MachineModel machineModel02 = getMachineModel("key02", "name02");
        when(mockMachineRepository.findAll()).thenReturn(List.of(machineModel01, machineModel02));
        List<MachineModel> all = machineService.getAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(machineModel01));
        assertTrue(all.contains(machineModel02));
    }

    @Test
    void getMachineListFromCSVFileShouldReturnAllTheDataFromTheFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:data/machines.csv");
        FileInputStream inputStream = new FileInputStream(file);
        List<MachineModel> fromCSVFile = machineService.getMachineListFromCSVFile(inputStream);
        MachineModel machineModel = new MachineModel("arnold", "arnold");

        assertEquals(5, fromCSVFile.size());
        assertTrue(fromCSVFile.contains(machineModel));
    }

    @Test
    void getMachineListFromCSVFileShouldDueFileNotFound() {
        CsvParseException ex = assertThrows(CsvParseException.class, () -> machineService.getMachineListFromCSVFile(null));
        assertEquals("Exception occurred when parsing CSV!", ex.getMessage());
    }

    private MachineModel getMachineModel(final String key, final String name) {
        return new MachineModel(key, name);
    }
}