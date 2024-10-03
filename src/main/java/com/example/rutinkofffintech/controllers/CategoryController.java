package com.example.rutinkofffintech.controllers;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/places/categories")
@Validated // Включаем валидацию
public class CategoryController {

    private final DataStore<Category> categoryStore;

    public CategoryController(DataStore<Category> categoryStore) {
        this.categoryStore = categoryStore;
    }

    @GetMapping
    public Map<String, Category> getAllCategories() {
        return categoryStore.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable @NotBlank String id) { // Используем @NotBlank для валидации id
        Category category = categoryStore.get(id);
        if (category == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(category); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @NotBlank String id) { // Используем @NotBlank для валидации id
        if (!categoryStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        categoryStore.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody Category category) { // Используем @Valid для валидации тела запроса
        if (categoryStore.exists(category.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        categoryStore.save(category.getId(), category);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable @NotBlank String id, @Valid @RequestBody Category category) { // Валидация и для id, и для тела запроса
        if (!categoryStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        categoryStore.update(id, category);
        return ResponseEntity.ok().build(); // 200 OK
    }
}
