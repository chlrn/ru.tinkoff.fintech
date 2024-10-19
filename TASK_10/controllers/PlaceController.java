package com.example.rutinkofffintech.TASK_10.controllers;

import com.example.rutinkofffintech.TASK_10.dto.Place;
import com.example.rutinkofffintech.TASK_10.repository.PlaceRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    // Получение всех мест
    @GetMapping
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    // Получение места по ID
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Создание нового места
    @PostMapping
    public ResponseEntity<?> createPlace(@Valid @RequestBody Place place, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        placeRepository.save(place);
        return ResponseEntity.status(HttpStatus.CREATED).body(place);
    }

    // Обновление существующего места
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id, @Valid @RequestBody Place updatedPlace, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        return placeRepository.findById(id)
                .map(existingPlace -> {
                    existingPlace.setName(updatedPlace.getName());
                    existingPlace.setSlug(updatedPlace.getSlug());
                    existingPlace.setDescription(updatedPlace.getDescription());
                    existingPlace.setCity(updatedPlace.getCity());
                    existingPlace.setAddress(updatedPlace.getAddress());
                    existingPlace.setLatitude(updatedPlace.getLatitude());
                    existingPlace.setLongitude(updatedPlace.getLongitude());
                    placeRepository.save(existingPlace);
                    return ResponseEntity.ok(existingPlace);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Удаление места
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(place -> {
                    placeRepository.delete(place);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
