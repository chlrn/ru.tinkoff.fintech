package com.example.rutinkofffintech.task_11.snapshot;

import com.example.rutinkofffintech.task_11.dto.Category;

public class CategorySnapshot implements Snapshot<Category> {
    private final Category categoryState;

    public CategorySnapshot(Category category) {
        this.categoryState = new Category(category.getId(), category.getName());
    }

    @Override
    public Category restore() {
        return new Category(categoryState.getId(), categoryState.getName());
    }
}
