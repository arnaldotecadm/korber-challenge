package be.com.arcasoftwares.model;

import java.util.Objects;

public class ParameterProperty {
    private final String property;
    private final Double value;

    public ParameterProperty(final String property, final Double value) {
        this.property = property;
        this.value = value;
    }
    public String getProperty() {
        return property;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterProperty)) return false;
        final ParameterProperty that = (ParameterProperty) o;
        return Objects.equals(getProperty(), that.getProperty()) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProperty(), getValue());
    }
}
