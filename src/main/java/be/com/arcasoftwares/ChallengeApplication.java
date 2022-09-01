package be.com.arcasoftwares;

import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.service.MachineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class ChallengeApplication implements CommandLineRunner {

    private final MachineService machineService;

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }

    public ChallengeApplication(MachineService machineService) {
        this.machineService = machineService;
    }

    @Override
    public void run(final String... args) throws Exception {
        InputStream in = getMachineDataFile("data/machines.csv");
        List<MachineModel> machineModelList = getModelListFromFile(in);
        persistMachineModelList(machineModelList);
    }

    public void persistMachineModelList(final List<MachineModel> machineModelList) {
        machineModelList.forEach(machineService::save);
    }

    public List<MachineModel> getModelListFromFile(final InputStream in) {
        return machineService.getMachineListFromCSVFile(in);
    }

    public InputStream getMachineDataFile(String fileLocation) {
        return getClass().getClassLoader().getResourceAsStream(fileLocation);
    }
}
