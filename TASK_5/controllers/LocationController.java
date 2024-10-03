package com.example.rutinkofffintech.controllers;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final DataStore<Location> locationStore;

    public LocationController(DataStore<Location> locationStore) {
        this.locationStore = locationStore;
    }


    @GetMapping
    public Map<String, Location> getAllLocations() {
        return locationStore.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable String id) {
        Location location = locationStore.get(id);
        if (location == null) {
            return ResponseEntity.notFound().build(); // Возвращаем 404
        }
        return ResponseEntity.ok(location); // Возвращаем 200 и объект
    }


    @PostMapping
    public ResponseEntity<Void> createLocation(@RequestBody Location location) {
        if (location == null || location.getId() == null || location.getName() == null || location.getName().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (locationStore.exists(location.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        locationStore.save(location.getId(), location);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLocation(@PathVariable String id, @RequestBody Location location) {
        if (id == null || id.isEmpty() || location == null || location.getName() == null || location.getName().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (!locationStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        locationStore.update(id, location);
        return ResponseEntity.ok().build(); // 200 OK
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        if (!locationStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        locationStore.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
