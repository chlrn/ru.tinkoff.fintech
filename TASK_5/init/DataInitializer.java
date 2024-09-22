package com.example.rutinkofffintech.TASK_5.init;

import com.example.rutinkofffintech.TASK_5.datastore.DataStore;
import com.example.rutinkofffintech.TASK_5.dto.Category;
import com.example.rutinkofffintech.TASK_5.dto.Location;
import com.example.rutinkofffintech.TASK_5.logging.LogExecutionTime;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class DataInitializer {

    private final DataStore<Category> categoryStore;
    private final DataStore<Location> locationStore;
    private final RestTemplate restTemplate;

    private int currentLocationId = 1; // Счетчик для ID локаций

    public DataInitializer(DataStore<Category> categoryStore, DataStore<Location> locationStore, RestTemplate restTemplate) {
        this.categoryStore = categoryStore;
        this.locationStore = locationStore;
        this.restTemplate = restTemplate;
    }

    @LogExecutionTime
    @PostConstruct
    public void init() {
        loadCategories();
        loadLocations();
    }

    private void loadCategories() {
        String url = "https://kudago.com/public-api/v1.4/place-categories";
        Category[] categories = restTemplate.getForObject(url, Category[].class);
        if (categories != null) {
            Arrays.stream(categories)
                    .filter(category -> category.getId() != null)
                    .forEach(category -> categoryStore.save(category.getId().toString(), category));
        }
    }

    private void loadLocations() {
        String url = "https://kudago.com/public-api/v1.4/locations/";
        Location[] locations = restTemplate.getForObject(url, Location[].class);
        if (locations != null) {
            for (Location location : locations) {
                // Присвоение уникального ID
                location.setId(String.valueOf(currentLocationId));
                currentLocationId++; // Инкремент счетчика
                locationStore.save(location.getId(), location);
            }
        }
    }
}
