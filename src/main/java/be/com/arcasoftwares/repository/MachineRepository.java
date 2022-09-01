package be.com.arcasoftwares.repository;

import be.com.arcasoftwares.model.MachineModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class MachineRepository implements KorberRepository<MachineModel> {

    private final Set<MachineModel> machineModelSet;

    public MachineRepository() {
        this.machineModelSet = new HashSet<>();
    }

    public MachineModel upsert(final MachineModel model) {
        this.machineModelSet.remove(model);
        this.machineModelSet.add(model);
        return model;
    }

    public List<MachineModel> findAll() {
        return new ArrayList<>(this.machineModelSet);
    }

    public Optional<MachineModel> findByKey(final String key) {
        return this.machineModelSet.stream().filter(item -> item.getKey().equals(key)).findAny();
    }
}
