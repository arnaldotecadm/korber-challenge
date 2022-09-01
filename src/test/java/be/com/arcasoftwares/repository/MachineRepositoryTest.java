package be.com.arcasoftwares.repository;

import be.com.arcasoftwares.model.MachineModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MachineRepositoryTest {

    MachineRepository machineRepository;

    @BeforeEach
    void setup() {
        machineRepository = new MachineRepository();
    }

    @Test
    void upsertShoudInsertSucessfully() {
        MachineModel upsert = this.machineRepository.upsert(getMachineModel("key", "name"));

        assertEquals("key", upsert.getKey());
        assertEquals("name", upsert.getName());
    }

    @Test
    void findAllShouldReturnAllItems() {
        MachineModel model01 = getMachineModel("key", "name01");
        this.machineRepository.upsert(model01);
        MachineModel model02 = getMachineModel("key01", "name02");
        this.machineRepository.upsert(model02);
        MachineModel model03 = getMachineModel("key002", "name02");
        this.machineRepository.upsert(model03);

        List<MachineModel> all = this.machineRepository.findAll();
        assertEquals(3, all.size());
        assertTrue(all.contains(model01));
        assertTrue(all.contains(model02));
        assertTrue(all.contains(model03));
    }

    @Test
    void findByKeyShouldReturnItem() {
        MachineModel model01 = getMachineModel("key", "name");
        this.machineRepository.upsert(model01);
        Optional<MachineModel> byKey = this.machineRepository.findByKey("key");

        assertTrue(byKey.isPresent());
        assertEquals("key", byKey.get().getKey());
    }

    @Test
    void findByKeyShouldNotReturnItem() {
        MachineModel model01 = getMachineModel("key", "name1");
        this.machineRepository.upsert(model01);
        Optional<MachineModel> byKey = this.machineRepository.findByKey("key01");

        assertFalse(byKey.isPresent());
    }

    private MachineModel getMachineModel(final String key, final String name) {
        return new MachineModel(key, name);
    }
}