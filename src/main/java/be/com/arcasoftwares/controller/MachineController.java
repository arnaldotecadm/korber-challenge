package be.com.arcasoftwares.controller;

import be.com.arcasoftwares.model.MachineModel;
import be.com.arcasoftwares.service.MachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("machine")
public class MachineController {

    private final MachineService machineService;

    public MachineController(final MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping
    MachineModel persist(@RequestBody final MachineModel machineModel) {
        return this.machineService.save(machineModel);
    }

    @GetMapping
    List<MachineModel> getAll() {
        return this.machineService.getAll();
    }
}
