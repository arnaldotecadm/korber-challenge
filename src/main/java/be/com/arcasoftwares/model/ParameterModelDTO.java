package be.com.arcasoftwares.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParameterModelDTO {
    private final String machineKey;
    private final Map<String, Double> parameters;
    private final Date date;

    public ParameterModelDTO(final String machineKey, final Map<String, Double> parameters, final Date date) {
        this.machineKey = machineKey;
        this.parameters = parameters;
        this.date = date;
    }

    public ParameterModelDTO(final ParameterModel model) {
        Map<String, Double> values = new HashMap<>();
        model.getParameters().forEach(p ->
                values.put(p.getProperty(), p.getValue())
        );
        this.machineKey = model.getMachineKey();
        this.parameters = values;
        this.date = model.getDate();
    }

    public String getMachineKey() {
        return machineKey;
    }

    public Map<String, Double> getParameters() {
        return parameters;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterModelDTO)) return false;
        final ParameterModelDTO that = (ParameterModelDTO) o;
        return getMachineKey().equals(that.getMachineKey()) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMachineKey(), date);
    }

}
