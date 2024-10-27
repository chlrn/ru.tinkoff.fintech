package com.example.rutinkofffintech.task_9.controllers;

import com.example.rutinkofffintech.task_9.dto.Event;
import com.example.rutinkofffintech.task_9.init.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Event>>> getEvents(
            @RequestParam double budget,
            @RequestParam String currency,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        return eventService.getEvents(budget, currency, dateFrom, dateTo)
                .thenApply(events -> ResponseEntity.ok(events))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
