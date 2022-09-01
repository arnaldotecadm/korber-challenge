package be.com.arcasoftwares.model;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MachineModelTest {

    @Test
    void testConstructor() {
        MachineModel model = new MachineModel("key", "name");
        assertNotNull(model);
    }

    @Test
    void testGetters() {
        MachineModel model = new MachineModel("key", "name");
        assertEquals("key", model.getKey());
        assertEquals("name", model.getName());
    }

    @Test
    void testEqualsShouldBeEquals() {
        MachineModel model01 = new MachineModel("key", "name");
        MachineModel model02 = new MachineModel("key", "name");
        assertEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeEquals() {
        MachineModel model01 = new MachineModel("key", "name");
        MachineModel model02 = new MachineModel("key01", "name");
        assertNotEquals(model01, model02);
    }

    @Test
    void testEqualsShouldNotBeEqualsOtherType() {
        MachineModel model01 = new MachineModel("key", "name");
        boolean equals = model01.equals("");
        assertFalse(equals);
    }

    @Test
    void testHashCode() {
        MachineModel model = new MachineModel("key", "name");
        int hashCode = Objects.hash("key");
        assertEquals(hashCode, model.hashCode());
    }
}