package com.example.rutinkofffintech.TASK_5.controllers;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/places/categories")
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
    public ResponseEntity<Category> getCategory(@PathVariable(required = false) String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        Category category = categoryStore.get(id);
        if (category == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(category); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(required = false) String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (!categoryStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        categoryStore.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody Category category) {
        if (category == null || category.getId() == null || category.getId().isEmpty() ||
                category.getName() == null || category.getName().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (categoryStore.exists(category.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        categoryStore.save(category.getId(), category);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable String id, @RequestBody Category category) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (category == null || category.getId() == null || category.getName() == null || category.getName().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (!categoryStore.exists(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        categoryStore.update(id, category);
        return ResponseEntity.ok().build(); // 200 OK
    }


    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}
