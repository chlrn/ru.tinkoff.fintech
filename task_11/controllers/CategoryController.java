package com.example.rutinkofffintech.task_11.controllers;

import com.example.rutinkofffintech.task_11.datastore.DataStore;
import com.example.rutinkofffintech.task_11.dto.Category;
import com.example.rutinkofffintech.task_11.snapshot.Snapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/places/categories")
public class CategoryController {
    private final DataStore<Category> categoryStore;

    public CategoryController(DataStore<Category> categoryStore) {
        this.categoryStore = categoryStore;
    }

    @PostMapping("/{id}/snapshot")
    public ResponseEntity<Void> createSnapshot(@PathVariable String id) {
        Category category = categoryStore.get(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        categoryStore.saveSnapshot(new CategorySnapshot(category)); // Создаем снимок категории
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/snapshots")
    public ResponseEntity<List<Category>> getSnapshots(@PathVariable String id) {
        List<Snapshot<Category>> snapshots = categoryStore.getAllSnapshots(id);
        List<Category> snapshotStates = snapshots.stream()
                .map(Snapshot::restore)
                .collect(Collectors.toList());
        return ResponseEntity.ok(snapshotStates);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreLastSnapshot(@PathVariable String id) {
        if (!categoryStore.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryStore.restoreLastSnapshot(id); // Восстанавливаем из последнего снимка
        return ResponseEntity.ok().build();
    }
}
