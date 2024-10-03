package com.example.rutinkofffintech;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)

@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataStore<Location> locationStore;

    private final String LOCATION_ID = "1";
    private Location testLocation;

    @BeforeEach
    void setUp() {
        testLocation = new Location();
        testLocation.setId(LOCATION_ID);
        testLocation.setName("Test Location");
    }

    @Test
    public void getAllLocationsTest() throws Exception {
        when(locationStore.getAll()).thenReturn(Map.of(LOCATION_ID, testLocation));

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LOCATION_ID + ".name").value("Test Location")); // Изменяем путь на Map-стиль
    }


    @Test
    public void getLocationByIdTest() throws Exception {
        when(locationStore.get(LOCATION_ID)).thenReturn(testLocation);

        mockMvc.perform(get("/api/v1/locations/{id}", LOCATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Location"));
    }

    @Test
    public void createLocationTest() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Location\"}"))
                .andExpect(status().isCreated()); // Ожидаем статус 201
        verify(locationStore, times(1)).save(eq(LOCATION_ID), any(Location.class));
    }


    @Test
    public void updateLocationTest() throws Exception {
        when(locationStore.exists(LOCATION_ID)).thenReturn(true); // Проверка существования объекта

        mockMvc.perform(put("/api/v1/locations/{id}", LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Updated Location\"}"))
                .andExpect(status().isOk());
        verify(locationStore, times(1)).update(eq(LOCATION_ID), any(Location.class));
    }


    @Test
    public void deleteLocationTest() throws Exception {
        when(locationStore.exists(LOCATION_ID)).thenReturn(true); // Мокируем существование объекта

        mockMvc.perform(delete("/api/v1/locations/{id}", LOCATION_ID))
                .andExpect(status().isNoContent()); // Ожидаем статус 204
        verify(locationStore, times(1)).delete(LOCATION_ID);
    }

    @Test
    public void createLocationWithExistingIdTest() throws Exception {
        when(locationStore.exists(LOCATION_ID)).thenReturn(true); // Мокируем существование объекта

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Location\"}"))
                .andExpect(status().isConflict()); // Ожидаем статус 409
    }


    @Test
    public void updateLocationWithInvalidDataTest() throws Exception {
        mockMvc.perform(put("/api/v1/locations/{id}", LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"\"}")) // Пустое имя
                .andExpect(status().isBadRequest()); // Ожидаем статус 400
    }


    @Test
    public void getLocationNotFoundTest() throws Exception {
        when(locationStore.get(LOCATION_ID)).thenReturn(null); // Мокируем отсутствие объекта

        mockMvc.perform(get("/api/v1/locations/{id}", LOCATION_ID))
                .andExpect(status().isNotFound()); // Ожидаем статус 404
    }


    @Test
    public void updateNonExistingLocationTest() throws Exception {
        when(locationStore.get(LOCATION_ID)).thenReturn(null);

        mockMvc.perform(put("/api/v1/locations/{id}", LOCATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Non-existing Location\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNonExistingLocationTest() throws Exception {
        when(locationStore.get(LOCATION_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/locations/{id}", LOCATION_ID))
                .andExpect(status().isNotFound());
    }
}
