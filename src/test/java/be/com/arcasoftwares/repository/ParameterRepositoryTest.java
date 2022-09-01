package be.com.arcasoftwares.repository;

import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterRepositoryTest {

    ParameterRepository parameterRepository;

    @BeforeEach
    void setup() {
        parameterRepository = new ParameterRepository();
    }

    @Test
    void upsertShoudInsertSucessfully() {
        Date date = new Date();
        ParameterModel upsert = this.parameterRepository.upsert(getParameterModel("key", date));

        assertEquals("key", upsert.getMachineKey());
        assertEquals(date, upsert.getDate());
        assertEquals("prop1", upsert.getParameters().get(0).getProperty());
        assertEquals(5.0, upsert.getParameters().get(0).getValue());
    }

    @Test
    void findAllShouldReturnAllItems() {
        ParameterModel model01 = getParameterModel("key", new Date());
        this.parameterRepository.upsert(model01);
        ParameterModel model02 = getParameterModel("key01", new Date());
        this.parameterRepository.upsert(model02);
        ParameterModel model03 = getParameterModel("key002", new Date());
        this.parameterRepository.upsert(model03);

        List<ParameterModel> all = this.parameterRepository.findAll();
        assertEquals(3, all.size());
        assertTrue(all.contains(model01));
        assertTrue(all.contains(model02));
        assertTrue(all.contains(model03));
    }

    @Test
    void findByKeyShouldReturnItem() {
        ParameterModel model01 = getParameterModel("key", new Date());
        this.parameterRepository.upsert(model01);
        Optional<ParameterModel> byKey = this.parameterRepository.findByKey("key");

        assertTrue(byKey.isPresent());
        assertEquals("key", byKey.get().getMachineKey());
    }

    @Test
    void resetShouldDeleteAllEntries() {
        ParameterModel model01 = getParameterModel("key", new Date());
        this.parameterRepository.upsert(model01);
        Optional<ParameterModel> byKey = this.parameterRepository.findByKey("key");

        assertTrue(byKey.isPresent());
        assertEquals("key", byKey.get().getMachineKey());

        parameterRepository.removeAllEntries();

        List<ParameterModel> all = parameterRepository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void findByKeyShouldNotReturnItem() {
        ParameterModel model01 = getParameterModel("key", new Date());
        this.parameterRepository.upsert(model01);
        Optional<ParameterModel> byKey = this.parameterRepository.findByKey("key01");

        assertFalse(byKey.isPresent());
    }

    private ParameterModel getParameterModel(final String key, final Date d) {
        return new ParameterModel(key, List.of(new ParameterProperty("prop1", 5.0)), d);
    }
}