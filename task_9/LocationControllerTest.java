package com.example.rutinkofffintech.task_9;

import com.example.rutinkofffintech.task_9.datastore.DataStore;
import com.example.rutinkofffintech.task_9.dto.Location;
import com.example.rutinkofffintech.task_9.controllers.LocationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private DataStore<Location> locationStore;

    @BeforeEach
    void setUp() {
        locationStore = Mockito.mock(DataStore.class);
    }

    @Test
    void testGetAllLocations() throws Exception {
        Map<String, Location> locations = new HashMap<>();
        Location location = new Location();
        location.setId("1");
        location.setName("Test Location");
        locations.put("1", location);

        when(locationStore.getAll()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLocation_Success() throws Exception {
        Location location = new Location();
        location.setId("1");
        location.setName("Test Location");

        when(locationStore.get("1")).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLocation_NotFound() throws Exception {
        when(locationStore.get("2")).thenReturn(null);

        mockMvc.perform(get("/api/v1/locations/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateLocation_Success() throws Exception {
        Location location = new Location();
        location.setId("1");
        location.setName("Test Location");

        when(locationStore.exists(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"Test Location\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateLocation_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"\",\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}

