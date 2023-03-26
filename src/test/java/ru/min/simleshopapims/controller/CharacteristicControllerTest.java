package ru.min.simleshopapims.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.min.simleshopapims.SimpleShopApiMsApplication;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.service.CharacteristicService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SimpleShopApiMsApplication.class})
class CharacteristicControllerTest {

    private static final String NAME = "nice";
    private Long id = 1L;
    private Characteristic characteristic;
    private ObjectMapper objectMapper = new ObjectMapper();

    String token;

    String body = """
            {
                "username" : "admin",
                "password": "123QWEasd" 
            }
            """;

    @MockBean
    private CharacteristicService characteristicServiceMock;

    //@InjectMocks
    CharacteristicController out;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() throws Exception {
        characteristic = new Characteristic(NAME);
        characteristic.setId(id);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"access_token\": \"", "");
        token = response.replace("\"}", "");
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus200WhenCreateCharacteristic() throws Exception {
        when(characteristicServiceMock.createCharacteristic(any())).thenReturn(characteristic);
        mockMvc.perform(post("http://localhost:8080/characteristic")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(2))
                );
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus405WhenCreateCharacteristic() throws Exception {
        when(characteristicServiceMock.createCharacteristic(any())).thenThrow(MyValidationException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isMethodNotAllowed()
                );
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus409WhenCreateCharacteristic() throws Exception {
        when(characteristicServiceMock.createCharacteristic(any())).thenThrow(NotFoundByIdException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void shouldReturnStatus401WhenCreateCharacteristicUnauthorized() throws Exception {
        when(characteristicServiceMock.updateCharacteristic(any(), any())).thenThrow(NotFoundByIdException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isUnauthorized()
                );
    }


    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus200WhenUpdateCharacteristic() throws Exception {
        when(characteristicServiceMock.updateCharacteristic(any(), any())).thenReturn(characteristic);
        mockMvc.perform(post("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(2))
                );
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus404WhenUpdateCharacteristic() throws Exception {
        when(characteristicServiceMock.updateCharacteristic(any(), any())).thenThrow(NotFoundByIdException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus405WhenUpdateCharacteristic() throws Exception {
        when(characteristicServiceMock.updateCharacteristic(any(), any())).thenThrow(MyValidationException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isMethodNotAllowed()
                );
    }

    @Test
    void shouldReturnStatus401WhenUpdateCharacteristic() throws Exception {
        when(characteristicServiceMock.updateCharacteristic(any(), any())).thenThrow(MyValidationException.class);
        mockMvc.perform(post("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus404WhenDeleteCharacteristic() throws Exception {
        doThrow(NotFoundByIdException.class).when(characteristicServiceMock).deleteCharacteristicById(any());
        mockMvc.perform(delete("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void shouldReturnStatus401WhenDeleteCharacteristic() throws Exception {
        doThrow(NotFoundByIdException.class).when(characteristicServiceMock).deleteCharacteristicById(any());
        mockMvc.perform(delete("http://localhost:8080/characteristic/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isUnauthorized()
                );
    }


    @WithMockUser(value = "admin")
    @Test
    void shouldReturnStatus200WhenFindAllCharacteristics() throws Exception {
        List<Characteristic> characteristicList = new ArrayList<>(List.of(characteristic));
        when(characteristicServiceMock.findAll()).thenReturn(characteristicList);
        mockMvc.perform(get("http://localhost:8080/characteristic/all", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void shouldReturnStatus401WhenFindAllCharacteristics() throws Exception {
        List<Characteristic> characteristicList = new ArrayList<>(List.of(characteristic));
        when(characteristicServiceMock.findAll()).thenReturn(characteristicList);
        mockMvc.perform(get("http://localhost:8080/characteristic/all", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(characteristic)))
                .andExpectAll(
                        status().isUnauthorized()
                );
    }
}