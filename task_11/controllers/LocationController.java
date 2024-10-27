package com.example.rutinkofffintech.task_11.controllers;

import com.example.rutinkofffintech.task_11.datastore.DataStore;
import com.example.rutinkofffintech.task_11.dto.Location;
import com.example.rutinkofffintech.task_11.snapshot.Snapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final DataStore<Location> locationStore;

    public LocationController(DataStore<Location> locationStore) {
        this.locationStore = locationStore;
    }

    @PostMapping("/{id}/snapshot")
    public ResponseEntity<Void> createSnapshot(@PathVariable String id) {
        Location location = locationStore.get(id);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        locationStore.saveSnapshot(new LocationSnapshot(location)); // Создаем снимок локации
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/snapshots")
    public ResponseEntity<List<Location>> getSnapshots(@PathVariable String id) {
        List<Snapshot<Location>> snapshots = locationStore.getAllSnapshots(id);
        List<Location> snapshotStates = snapshots.stream()
                .map(Snapshot::restore)
                .collect(Collectors.toList());
        return ResponseEntity.ok(snapshotStates);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreLastSnapshot(@PathVariable String id) {
        if (!locationStore.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        locationStore.restoreLastSnapshot(id); // Восстанавливаем из последнего снимка
        return ResponseEntity.ok().build();
    }
}
