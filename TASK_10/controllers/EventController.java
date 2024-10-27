package com.example.rutinkofffintech.TASK_10.controllers;

import com.example.rutinkofffintech.TASK_10.dto.Event;
import com.example.rutinkofffintech.TASK_10.dto.Place;
import com.example.rutinkofffintech.TASK_10.repository.EventRepository;
import com.example.rutinkofffintech.TASK_10.repository.PlaceRepository;
import com.example.rutinkofffintech.TASK_10.specifications.EventSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public EventController(EventRepository eventRepository, PlaceRepository placeRepository) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    // Поиск событий с фильтрацией
    @GetMapping
    public List<Event> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        Specification<Event> spec = Specification.where(null);

        if (name != null) spec = spec.and(EventSpecifications.hasName(name));
        if (placeId != null) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new EntityNotFoundException("Place not found"));
            spec = spec.and(EventSpecifications.hasPlace(place));
        }
        if (fromDate != null && toDate != null) {
            spec = spec.and(EventSpecifications.betweenDates(Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate)));
        }

        return eventRepository.findAll(spec);
    }

    // Создание нового события
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        if (!placeRepository.existsById(event.getPlace().getId())) {
            throw new IllegalArgumentException("Place not found for the event");
        }

        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Удаление события
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    // Обновление события
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody Event updatedEvent, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (!placeRepository.existsById(updatedEvent.getPlace().getId())) {
                        throw new IllegalArgumentException("Place not found for the event");
                    }
                    existingEvent.setName(updatedEvent.getName());
                    existingEvent.setDate(updatedEvent.getDate());
                    existingEvent.setPlace(updatedEvent.getPlace());
                    eventRepository.save(existingEvent);
                    return ResponseEntity.ok(existingEvent);
                })
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }
}
