package be.com.arcasoftwares.repository;

import be.com.arcasoftwares.model.ParameterModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class ParameterRepository implements KorberRepository<ParameterModel> {

    private final Set<ParameterModel> parameterModelSet;

    public ParameterRepository() {
        this.parameterModelSet = new HashSet<>();
    }

    public ParameterModel upsert(final ParameterModel model) {
        this.parameterModelSet.remove(model);
        this.parameterModelSet.add(model);
        return model;
    }

    public List<ParameterModel> findAll() {
        return new ArrayList<>(this.parameterModelSet);
    }

    public Optional<ParameterModel> findByKey(String key) {
        return this.parameterModelSet.stream().filter(item -> item.getMachineKey().equals(key)).findAny();
    }

    public void removeAllEntries(){
        this.parameterModelSet.clear();
    }
}
