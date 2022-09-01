package be.com.arcasoftwares.model;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ParameterModelTest {

    @Test
    void testConstructor() {
        Date date = new Date();
        ParameterProperty param01 = new ParameterProperty("prop01", 3.0);
        ParameterModel model = new ParameterModel("key01", List.of(param01), date);

        assertNotNull(model);
        assertEquals("key01", model.getMachineKey());
        assertEquals(date, model.getDate());
        assertTrue(model.getParameters().contains(param01));
    }

    @Test
    void testConstructorFromModelDTO() {
        Date date = new Date();
        ParameterProperty param01 = new ParameterProperty("prop01", 3.0);
        ParameterModel model = new ParameterModel("key01", List.of(param01), date);
        ParameterModelDTO parameterModelDTO = new ParameterModelDTO(model);
        ParameterModel modelFromDTO = new ParameterModel(parameterModelDTO);

        assertNotNull(modelFromDTO);
        assertEquals("key01", modelFromDTO.getMachineKey());
        assertTrue(modelFromDTO.getParameters().contains(param01));
    }

    @Test
    void testEqualsShouldBeTheSame() {
        Date date = new Date();
        ParameterProperty param01 = new ParameterProperty("prop01", 3.0);
        ParameterModel model01 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModel model02 = new ParameterModel("key01", List.of(param01), date);

        assertEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeTheSame() {
        Date date = new Date();
        Date date1 = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MINUTE, 1);
        date.setTime(c1.getTimeInMillis());
        ParameterProperty param01 = new ParameterProperty("prop01", 3.0);
        ParameterModel model01 = new ParameterModel("key01", Collections.emptyList(), date);
        ParameterModel model02 = new ParameterModel("key01", List.of(param01), date1);

        assertNotEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeTheSameOtherType() {
        Date date = new Date();
        ParameterModel model = new ParameterModel("key01", Collections.emptyList(), date);
        boolean equals = model.equals("");
        assertFalse(equals);
    }

    @Test
    void testHashCode() {
        Date date = new Date();
        ParameterModel model = new ParameterModel("key01", Collections.emptyList(), date);
        int hashCode = Objects.hash("key01", date);
        assertEquals(hashCode, model.hashCode());
    }
}