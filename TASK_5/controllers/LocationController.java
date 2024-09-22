package com.example.rutinkofffintech.TASK_5.controllers;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Location;
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
    public Location getLocation(@PathVariable String id) {
        return locationStore.get(id);
    }

    @PostMapping
    public void createLocation(@RequestBody Location location) {
        locationStore.save(location.getId(), location);
    }

    @PutMapping("/{id}")
    public void updateLocation(@PathVariable String id, @RequestBody Location location) {
        locationStore.update(id, location);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable String id) {
        locationStore.delete(id);
    }
}
