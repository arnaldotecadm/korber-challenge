package be.com.arcasoftwares;

import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.service.MachineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChallengeApplicationTest {

    private MachineService mockMachineService;
    private ChallengeApplication challengeApplication;

    @BeforeEach
    void setup() {
        mockMachineService = mock(MachineService.class);
        challengeApplication = new ChallengeApplication(mockMachineService);
    }

    @Test
    void mainShouldJustRunForCoveragePurposes() {
        Assertions.assertDoesNotThrow(() -> ChallengeApplication.main(new String[]{"noArgs"}));
    }

    @Test
    void runShouldThrowNoExceptions() throws Exception {
        when(mockMachineService.getMachineListFromCSVFile(any())).thenReturn(List.of(new MachineModel("key", "name")));
        challengeApplication.run();
        verify(mockMachineService, times(1)).getMachineListFromCSVFile(any());
        verify(mockMachineService, times(1)).save(any());
    }

    @Test
    void persistMachineModelListShouldPersistAnyItemInTheList() {
        List<MachineModel> modelList = List.of(
                new MachineModel("key01", "name01"),
                new MachineModel("key02", "name02"),
                new MachineModel("key03", "name03")
        );

        challengeApplication.persistMachineModelList(modelList);
        verify(mockMachineService, times(3)).save(any());
    }

    @Test
    void getModelListFromFileShouldRetrieveAllFilesFromTheLoadingFile() {
        List<MachineModel> modelList = List.of(
                new MachineModel("key01", "name01"),
                new MachineModel("key02", "name02"),
                new MachineModel("key03", "name03")
        );
        when(mockMachineService.getMachineListFromCSVFile(any())).thenReturn(modelList);
        List<MachineModel> listFromFile = challengeApplication.getModelListFromFile(mock(InputStream.class));

        assertEquals(3, listFromFile.size());
    }

    @Test
    void getMachineDataFileShouldReturnAFile() throws FileNotFoundException {
        InputStream dataFile = challengeApplication.getMachineDataFile("data/machines.csv");
        assertNotNull(dataFile);
    }

    @Test
    void getMachineDataFileShouldWhenFileNotFound() {
        InputStream dataFile = challengeApplication.getMachineDataFile("data/machines-not-found.csv");
        assertNull(dataFile);
    }
}