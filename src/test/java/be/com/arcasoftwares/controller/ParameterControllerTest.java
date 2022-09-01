package be.com.arcasoftwares.controller;

import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterModelDTO;
import be.com.arcasoftwares.model.ParameterProperty;
import be.com.arcasoftwares.service.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParameterControllerTest {

    private ParameterService mockParameterService;
    private ParameterController parameterController;

    @BeforeEach
    void setup() {
        mockParameterService = mock(ParameterService.class);
        parameterController = new ParameterController(mockParameterService);
    }

    @Test
    void persistShowSuccessFullyPersist() {
        Date date = new Date();
        ParameterModel model = getParameterModel("key01", date, "prop01", 5.0);
        ParameterModelDTO modelDTO = new ParameterModelDTO(model);
        when(mockParameterService.save(any())).thenReturn(model);
        ParameterModelDTO persist = parameterController.persist(modelDTO);

        assertEquals("key01", persist.getMachineKey());
        assertEquals(date, persist.getDate());
        assertNotNull(persist.getParameters().get("prop01"));
        assertEquals(5.0, persist.getParameters().get("prop01"));
    }

    @Test
    void getAll() {
        ParameterModel model01 = getParameterModel("key01", new Date());
        ParameterModel model02 = getParameterModel("key01", new Date());
        ParameterModel model03 = getParameterModel("key01", new Date());

        when(mockParameterService.getAll()).thenReturn(List.of(model01, model02, model03));

        List<ParameterModelDTO> all = parameterController.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void getLatest() {
        ParameterModel model = getParameterModel("key01", new Date());
        ParameterModelDTO modelDTO = new ParameterModelDTO(model);
        when(mockParameterService.getLatest()).thenReturn(List.of(modelDTO));

        List<ParameterModelDTO> latest = parameterController.getLatest();
        assertTrue(latest.contains(modelDTO));
    }

    @Test
    void getSummary() {
        Map<String, Map<String, Map<String, Double>>> summary = new HashMap<>();
        Map<String, Map<String, Double>> key = new HashMap<>();
        Map<String, Double> property = new HashMap<>();
        property.put("avg", 5.0);
        key.put("prop01", property);
        summary.put("key01", key);

        when(mockParameterService.getSummary(0)).thenReturn(summary);

        Map<String, Map<String, Map<String, Double>>> summaryFromService = parameterController.getSummary(0);

        assertEquals(summary, summaryFromService);
    }

    private ParameterModel getParameterModel(final String key, final Date d) {
        return getParameterModel(key, d, "prop1", 5.0);
    }

    private ParameterModel getParameterModel(final String key, final Date d, String prop, Double valor) {
        return new ParameterModel(key, List.of(new ParameterProperty(prop, valor)), d);
    }
}