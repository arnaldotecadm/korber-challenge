package be.com.arcasoftwares.controller;

import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterModelDTO;
import be.com.arcasoftwares.service.ParameterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("parameter")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(final ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping
    ParameterModelDTO persist(@RequestBody final ParameterModelDTO modelDTO) {
        ParameterModel parameterModel = new ParameterModel(modelDTO);
        ParameterModel save = parameterService.save(parameterModel);
        return new ParameterModelDTO(save);
    }

    @GetMapping
    List<ParameterModelDTO> getAll() {
        return this.parameterService.getAll().stream().map(ParameterModelDTO::new).collect(Collectors.toList());
    }

    @GetMapping("latest")
    List<ParameterModelDTO> getLatest() {
        return this.parameterService.getLatest();
    }

    @GetMapping("summary/{minutes}")
    Map<String, Map<String, Map<String, Double>>> getSummary(@PathVariable("minutes")int minutes) {
        return this.parameterService.getSummary(minutes);
    }
}
