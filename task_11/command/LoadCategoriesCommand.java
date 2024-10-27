package com.example.rutinkofffintech.task_11.command;

import com.example.rutinkofffintech.task_11.datastore.DataStore;
import com.example.rutinkofffintech.task_11.dto.Category;
import com.example.rutinkofffintech.task_11.init.DataInitializer;

public class LoadCategoriesCommand implements Command {
    private final DataInitializer initializer;
    private final DataStore<Category> categoryStore;

    public LoadCategoriesCommand(DataInitializer initializer, DataStore<Category> categoryStore) {
        this.initializer = initializer;
        this.categoryStore = categoryStore;
    }

    @Override
    public void execute() {
        initializer.loadCategories();
    }
}
