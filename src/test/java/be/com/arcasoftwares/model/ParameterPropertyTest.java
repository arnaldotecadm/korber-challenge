package be.com.arcasoftwares.model;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ParameterPropertyTest {

    @Test
    void testConstructor() {
        ParameterProperty model = new ParameterProperty("key", 3.0);
        assertNotNull(model);
    }

    @Test
    void testGetters() {
        ParameterProperty model = new ParameterProperty("key", 3.0);
        assertEquals("key", model.getProperty());
        assertEquals(3.0, model.getValue());
    }

    @Test
    void testEqualsShouldBeEquals() {
        ParameterProperty model01 = new ParameterProperty("key", 3.0);
        ParameterProperty model02 = new ParameterProperty("key", 3.0);
        boolean equals = model01.equals(model02);
        assertTrue(equals);
    }

    @Test
    void testEqualsShouldNotBeEquals() {
        ParameterProperty model01 = new ParameterProperty("key", 3.0);
        ParameterProperty model02 = new ParameterProperty("key01", 3.0);
        assertNotEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeEqualsValue() {
        ParameterProperty model01 = new ParameterProperty("key", 3.0);
        ParameterProperty model02 = new ParameterProperty("key", 5.0);
        assertNotEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeEqualsOtherType() {
        ParameterProperty model01 = new ParameterProperty("key", 3.0);
        boolean equals = model01.equals("");
        assertFalse(equals);
    }

    @Test
    void testHashCode() {
        ParameterProperty model = new ParameterProperty("key", 3.0);
        int hashCode = Objects.hash("key", 3.0);
        assertEquals(hashCode, model.hashCode());
    }
}