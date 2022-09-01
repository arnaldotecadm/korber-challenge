package integration;

import be.com.arcasoftwares.ChallengeApplication;
import be.com.arcasoftwares.model.MachineModel;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
class MachineControllerIntegrationTest {

    private final MockMvc mockMvc;

    @Autowired
    MachineControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void given_I_perform_a_post_request_the_webservice_answers_me_with_ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/machine")
                        .content(getJsonModel())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("machineName")));
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_default_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/machine")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void given_I_perform_a_get_request_the_webservice_answers_me_with_default_list_plus_one() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/machine")
                .content(getJsonModel())
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.get("/machine")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(6)));
    }

    private String getJsonModel() {
        MachineModel machineModel = new MachineModel("machineKey", "machineName");
        return new Gson().toJson(machineModel);
    }
}
