package be.com.arcasoftwares.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ParameterModel {
    private final String machineKey;
    private final List<ParameterProperty> parameters;
    private final Date date;

    public ParameterModel(final String machineKey, final List<ParameterProperty> parameters, final Date date) {
        this.machineKey = machineKey;
        this.parameters = parameters;
        this.date = date;
    }

    public ParameterModel(final ParameterModelDTO modelDTO) {
        List<ParameterProperty> parameterPropertyList = new ArrayList<>();
        modelDTO.getParameters().forEach((k, v) -> {
            ParameterProperty parameterProperty = new ParameterProperty(k, v);
            parameterPropertyList.add(parameterProperty);
        });
        this.machineKey = modelDTO.getMachineKey();
        this.parameters = parameterPropertyList;
        this.date = new Date();
    }

    public String getMachineKey() {
        return machineKey;
    }

    public List<ParameterProperty> getParameters() {
        return parameters;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterModel)) return false;
        final ParameterModel that = (ParameterModel) o;
        return getMachineKey().equals(that.getMachineKey()) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMachineKey(), date);
    }

}
