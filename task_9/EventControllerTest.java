package com.example.rutinkofffintech.task_9;

import com.example.rutinkofffintech.task_9.dto.Event;
import com.example.rutinkofffintech.task_9.init.EventService;
import com.example.rutinkofffintech.task_9.controllers.EventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = Mockito.mock(EventService.class);
    }

    @Test
    void testGetEvents_Success() throws Exception {
        Event event = new Event();
        event.setId("1");
        event.setName("Test Event");
        event.setPrice(100.0);

        when(eventService.getEvents(anyDouble(), anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(Collections.singletonList(event)));

        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "1000")
                        .param("currency", "USD")
                        .param("dateFrom", LocalDate.now().toString())
                        .param("dateTo", LocalDate.now().plusDays(7).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Event"));
    }

    @Test
    void testGetEvents_Error() throws Exception {
        when(eventService.getEvents(anyDouble(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "1000")
                        .param("currency", "USD")
                        .param("dateFrom", LocalDate.now().toString())
                        .param("dateTo", LocalDate.now().plusDays(7).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
