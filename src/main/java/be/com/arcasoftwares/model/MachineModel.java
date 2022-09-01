package be.com.arcasoftwares.model;

import java.util.Objects;

public class MachineModel {
    private final String key;
    private final String name;

    public MachineModel(final String key, final String name){
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineModel)) return false;
        final MachineModel that = (MachineModel) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
