package be.com.arcasoftwares.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParameterModelDTOTest {

    @Test
    void testConstructor() {
        Date date = new Date();
        ParameterProperty param01 = new ParameterProperty("prop01", 3.0);
        ParameterModel model = new ParameterModel("key01", List.of(param01), date);

        ParameterModelDTO modelDTO = new ParameterModelDTO(model);

        assertNotNull(modelDTO);
        assertEquals("key01", modelDTO.getMachineKey());
        assertEquals(date, modelDTO.getDate());
        assertNotNull(modelDTO.getParameters().get("prop01"));
    }

    @Test
    void testEqualsShouldBeTheSame() {
        Date date = new Date();
        ParameterModel model01 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModel model02 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModelDTO modelDTO01 = new ParameterModelDTO(model01);
        ParameterModelDTO modelDTO02 = new ParameterModelDTO(model02);

        assertEquals(modelDTO01, modelDTO02);
    }

    @Test
    void testEqualsShouldNotBeTheSame() {
        Date date = new Date();
        ParameterModel model01 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModel model02 = new ParameterModel("key02", Collections.emptyList(), date);
        ParameterModelDTO modelDTO01 = new ParameterModelDTO(model01);
        ParameterModelDTO modelDTO02 = new ParameterModelDTO(model02);

        assertNotEquals(modelDTO01, modelDTO02);
    }

    @Test
    void testEqualsShouldNotBeTheSameDifferenteDate() {
        Date date = new Date();
        Date date1 = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MINUTE, 1);
        date1.setTime(c1.getTimeInMillis());
        ParameterModel model01 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModel model02 = new ParameterModel("key01", Collections.emptyList(), date1);
        ParameterModelDTO modelDTO01 = new ParameterModelDTO(model01);
        ParameterModelDTO modelDTO02 = new ParameterModelDTO(model02);

        assertNotEquals(modelDTO01, modelDTO02);
    }

    @Test
    void testEqualsShouldNotBeTheSameDifferentTypes() {
        ParameterModel model = new ParameterModel("key", Collections.emptyList(), new Date());
        ParameterModelDTO modelDTO = new ParameterModelDTO(model);

        boolean equals = modelDTO.equals("");
        assertFalse(equals);
    }

    @Test
    void testHashCode() {
        Date date = new Date();
        ParameterModel model = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModelDTO modelDTO = new ParameterModelDTO(model);
        int hashCode = Objects.hash("key01", date);
        assertEquals(hashCode, modelDTO.hashCode());
    }
}