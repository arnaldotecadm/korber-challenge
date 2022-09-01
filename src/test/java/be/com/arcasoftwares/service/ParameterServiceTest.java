package be.com.arcasoftwares.service;

import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterModelDTO;
import be.com.arcasoftwares.model.ParameterProperty;
import be.com.arcasoftwares.repository.ParameterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParameterServiceTest {

    ParameterRepository mockParameterRepository;
    ParameterService parameterService;

    @BeforeEach
    void setup() {
        mockParameterRepository = mock(ParameterRepository.class);
        parameterService = new ParameterService(mockParameterRepository);
    }

    @Test
    void saveShouldSaveTheDataSuccessfully() {
        Date date = new Date();
        ParameterModel model = getParameterModel("key01", date);
        when(mockParameterRepository.upsert(any())).thenReturn(model);
        ParameterModel modelFromDB = parameterService.save(model);

        assertEquals("key01", modelFromDB.getMachineKey());
        assertEquals(date, modelFromDB.getDate());
    }

    @Test
    void getAllShouldRetrieveAllValues() {
        ParameterModel model01 = getParameterModel("key01", new Date());
        ParameterModel model02 = getParameterModel("key02", new Date());
        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02));
        List<ParameterModel> all = parameterService.getAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(model01));
        assertTrue(all.contains(model02));
    }

    @Test
    void getLatestShouldReturnTheLatestAcordingToTheDate() {
        ParameterModel model01 = getParameterModel("key01", new Date(), "prop1", 3.0);
        ParameterModel model02 = getParameterModel("key01", getDateAddingMinutes(3), "prop1", 5.0);
        ParameterModel model03 = getParameterModel("key01", getDateAddingMinutes(5), "prop1", 7.0);
        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02, model03));
        List<ParameterModelDTO> latest = parameterService.getLatest();

        assertEquals(1, latest.size());
        assertEquals(7, latest.get(0).getParameters().get("prop1"));
    }

    @Test
    void getLatestShouldReturnTheLatestAcordingToTheDateDifferentProps() {
        ParameterModel model01 = getParameterModel("key01", new Date(), "prop1", 3.0);
        ParameterModel model02 = getParameterModel("key01", getDateAddingMinutes(3), "prop2", 5.0);
        ParameterModel model03 = getParameterModel("key01", getDateAddingMinutes(5), "prop3", 7.0);
        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02, model03));
        List<ParameterModelDTO> latest = parameterService.getLatest();

        assertEquals(1, latest.size());
        assertNull(latest.get(0).getParameters().get("prop1"));
    }

    @Test
    void getSummaryShouldReturnMinMaxAndAverage() {
        ParameterModel model01 = getParameterModel("key01", new Date(), "prop1", 3.0);
        ParameterModel model02 = getParameterModel("key01", getDateAddingMinutes(3), "prop1", 5.0);
        ParameterModel model03 = getParameterModel("key01", getDateAddingMinutes(5), "prop1", 22.0);
        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02, model03));
        Map<String, Map<String, Map<String, Double>>> summary = parameterService.getSummary(0);

        Map<String, Map<String, Double>> map = summary.get("key01");

        assertNotNull(map);

        Map<String, Double> stringDoubleMap = map.get("prop1");

        assertNotNull(stringDoubleMap);

        Double avg = stringDoubleMap.get("avg");
        Double min = stringDoubleMap.get("min");
        Double max = stringDoubleMap.get("max");

        assertEquals(10.0, avg);
        assertEquals(3.0, min);
        assertEquals(22.0, max);
    }

    @Test
    void getSummaryShouldReturnMinMaxAndAverageInThePastXMinutes() {
        ParameterModel model01 = getParameterModel("key01", new Date(), "prop1", 3.0);
        ParameterModel model02 = getParameterModel("key01", getDateAddingMinutes(-2), "prop1", 5.0);
        ParameterModel model03 = getParameterModel("key01", getDateAddingMinutes(-5), "prop1", 22.0);
        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02, model03));
        Map<String, Map<String, Map<String, Double>>> summary = parameterService.getSummary(3);

        Map<String, Map<String, Double>> map = summary.get("key01");

        assertNotNull(map);

        Map<String, Double> stringDoubleMap = map.get("prop1");

        assertNotNull(stringDoubleMap);

        Double avg = stringDoubleMap.get("avg");
        Double min = stringDoubleMap.get("min");
        Double max = stringDoubleMap.get("max");

        assertEquals(4.0, avg);
        assertEquals(3.0, min);
        assertEquals(5.0, max);
    }

    @Test
    void getSummaryShouldReturnMinMaxAndAverageMultipleMatrixShoulProcessSuccessfully() {
        ParameterModel model01 = new ParameterModel("key01", List.of(
                getParam("prop01", 3.0),
                getParam("prop02", 4.0),
                getParam("prop03", 5.0)
        ), getDateAddingMinutes(1));

        ParameterModel model02 = new ParameterModel("key01", List.of(
                getParam("prop01", 5.0),
                getParam("prop02", 6.0),
                getParam("prop03", 7.0)
        ), getDateAddingMinutes(3));

        ParameterModel model03 = new ParameterModel("key01", List.of(
                getParam("prop01", 7.0),
                getParam("prop02", 11.0),
                getParam("prop03", 0.0)
        ), getDateAddingMinutes(5));

        ParameterModel model04 = new ParameterModel("key02", List.of(
                getParam("prop01", 1.0),
                getParam("prop02", 2.0),
                getParam("prop03", 3.0)
        ), getDateAddingMinutes(1));

        ParameterModel model05 = new ParameterModel("key03", List.of(
                getParam("prop01", 3.0),
                getParam("prop02", 4.0),
                getParam("prop03", 5.0)
        ), getDateAddingMinutes(1));


        when(mockParameterRepository.findAll()).thenReturn(List.of(model01, model02, model03, model04, model05));
        Map<String, Map<String, Map<String, Double>>> summary = parameterService.getSummary(0);

        Map<String, Map<String, Double>> key01 = summary.get("key01");
        Map<String, Map<String, Double>> key02 = summary.get("key02");
        Map<String, Map<String, Double>> key03 = summary.get("key03");

        assertNotNull(key01);
        assertNotNull(key02);
        assertNotNull(key03);

        //Assert values for key 01 Prop01
        Map<String, Double> stringDoubleMapKey01Prop01 = key01.get("prop01");
        assertEquals(5.0, stringDoubleMapKey01Prop01.get("avg"));
        assertEquals(3.0, stringDoubleMapKey01Prop01.get("min"));
        assertEquals(7.0, stringDoubleMapKey01Prop01.get("max"));

        //Assert values for key 01 Prop02
        Map<String, Double> stringDoubleMapKey01Prop02 = key01.get("prop02");
        assertEquals(7.0, stringDoubleMapKey01Prop02.get("avg"));
        assertEquals(4.0, stringDoubleMapKey01Prop02.get("min"));
        assertEquals(11.0, stringDoubleMapKey01Prop02.get("max"));

        //Assert values for key 01 Prop03
        Map<String, Double> stringDoubleMapKey01Prop03 = key01.get("prop03");
        assertEquals(4.0, stringDoubleMapKey01Prop03.get("avg"));
        assertEquals(0.0, stringDoubleMapKey01Prop03.get("min"));
        assertEquals(7.0, stringDoubleMapKey01Prop03.get("max"));

        //Assert values for key 02 Prop01
        Map<String, Double> stringDoubleMapKey02Prop01 = key02.get("prop01");
        assertEquals(1.0, stringDoubleMapKey02Prop01.get("avg"));
        assertEquals(1.0, stringDoubleMapKey02Prop01.get("min"));
        assertEquals(1.0, stringDoubleMapKey02Prop01.get("max"));

        //Assert values for key 03 Prop02
        Map<String, Double> stringDoubleMapKey03Prop02 = key03.get("prop02");
        assertEquals(4.0, stringDoubleMapKey03Prop02.get("avg"));
        assertEquals(4.0, stringDoubleMapKey03Prop02.get("min"));
        assertEquals(4.0, stringDoubleMapKey03Prop02.get("max"));
    }

    private ParameterProperty getParam(String prop, Double valor) {
        return new ParameterProperty(prop, valor);
    }

    private ParameterModel getParameterModel(final String key, final Date d) {
        return getParameterModel(key, d, "prop1", 5.0);
    }

    private ParameterModel getParameterModel(final String key, final Date d, String prop, Double valor) {
        return new ParameterModel(key, List.of(new ParameterProperty(prop, valor)), d);
    }

    private Date getDateAddingMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return date;
    }
}