package be.com.arcasoftwares.service;

import be.com.arcasoftwares.exception.CsvParseException;
import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.repository.KorberRepository;
import be.com.arcasoftwares.repository.MachineRepository;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachineService {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MachineService.class);

    private final KorberRepository<MachineModel> machineRepository;

    public MachineService(final MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public MachineModel save(final MachineModel model) {
        return this.machineRepository.upsert(model);
    }

    public List<MachineModel> getAll() {
        return this.machineRepository.findAll();
    }

    public List<MachineModel> getMachineListFromCSVFile(InputStream is) {
        try {
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return buffer.lines().skip(1).map(line -> {
                    String[] split = line.split(",");
                    return new MachineModel(split[0], split[1]);
                }).collect(Collectors.toList());
            }
        } catch (NullPointerException | IOException ex) {
            LOGGER.error("Exception occurred when parsing CSV!", ex);
            throw new CsvParseException("Exception occurred when parsing CSV!", ex);
        }
    }
}
