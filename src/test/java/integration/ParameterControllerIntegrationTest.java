package integration;

import be.com.arcasoftwares.ChallengeApplication;
import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterModelDTO;
import be.com.arcasoftwares.model.ParameterProperty;
import be.com.arcasoftwares.repository.ParameterRepository;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ParameterControllerIntegrationTest {

    private final ParameterRepository parameterRepository;
    private MockMvc mockMvc;

    @Autowired
    ParameterControllerIntegrationTest(final ParameterRepository parameterRepository, final MockMvc mockMvc) {
        this.parameterRepository = parameterRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void setup() {
        parameterRepository.removeAllEntries();
    }

    @Test
    void given_I_perform_a_post_request_the_webservice_answers_me_with_ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/parameter")
                        .content(getJsonModel())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.machineKey", Matchers.is("key01")));
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_a_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/parameter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_a_list() throws Exception {
        addDefaultItem(getJsonModel(1));
        addDefaultItem(getJsonModel(3));
        addDefaultItem(getJsonModel(5));
        mockMvc.perform(MockMvcRequestBuilders.get("/parameter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_the_latest() throws Exception {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MINUTE, -3);
        Date d1 = new Date(c1.getTimeInMillis());

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MINUTE, -1);
        Date d2 = new Date(c2.getTimeInMillis());

        addDefaultItem(getJsonModel("key01", d1, "prop01", 3.0));
        addDefaultItem(getJsonModel("key01", d2, "prop01", 5.0));

        mockMvc.perform(MockMvcRequestBuilders.get("/parameter/latest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].parameters.prop01", Matchers.is(5.0)));
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_the_summary_in_n_minutes() throws Exception {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MINUTE, -3);
        Date d1 = new Date(c1.getTimeInMillis());

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MINUTE, -1);
        Date d2 = new Date(c2.getTimeInMillis());

        addDefaultItem(getJsonModel("key01", d1, "prop01", 3.0));
        addDefaultItem(getJsonModel("key01", d2, "prop01", 5.0));

        mockMvc.perform(MockMvcRequestBuilders.get("/parameter/summary/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.key01.prop01.avg", Matchers.is(4.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.key01.prop01.min", Matchers.is(3.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.key01.prop01.max", Matchers.is(5.0)));
    }

    private void addDefaultItem(String jsonModel) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/parameter")
                .content(jsonModel)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private String getJsonModel(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -minutes);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return getJsonModel(date);
    }

    private String getJsonModel() {
        return this.getJsonModel(new Date());
    }

    private String getJsonModel(Date date) {
        return getJsonModel("key01", date, "prop01", 5.0);
    }

    private String getJsonModel(String key, Date date, String prop, Double value) {
        ParameterModel model = getParameterModel(key, date, prop, value);
        ParameterModelDTO modelDTO = new ParameterModelDTO(model);
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create().toJson(modelDTO);
    }

    private ParameterModel getParameterModel(final String key, final Date c, String prop, Double valor) {
        return new ParameterModel(key, List.of(new ParameterProperty(prop, valor)), c);
    }

}
