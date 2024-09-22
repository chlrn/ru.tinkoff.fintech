package com.example.rutinkofffintech.TASK_5.controllers;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Category;
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
    public Category getCategory(@PathVariable String id) {
        return categoryStore.get(id);
    }

    @PostMapping
    public void createCategory(@RequestBody Category category) {
        categoryStore.save(category.getId(), category);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable String id, @RequestBody Category category) {
        categoryStore.update(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) {
        categoryStore.delete(id);
    }
}
