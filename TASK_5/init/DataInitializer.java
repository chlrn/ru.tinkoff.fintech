package com.example.rutinkofffintech.init;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import com.example.rutinkofffintech.dto.Location;
import com.example.rutinkofffintech.logging.LogExecutionTime;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;

@Component
@Data
@RequiredArgsConstructor
public class DataInitializer {

    private final DataStore<Category> categoryStore;
    private final DataStore<Location> locationStore;

    @Value("${categories.url}")
    private String categoriesUrl;

    @Value("${locations.url}")
    private String locationsUrl;

    private int currentLocationId = 1; // Счетчик для ID локаций
    private final ObjectMapper objectMapper = new ObjectMapper();

    @LogExecutionTime
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        loadCategories();
        loadLocations();
    }

    private void loadCategories() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(categoriesUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    Category[] categories = objectMapper.readValue(response.getEntity().getContent(), Category[].class);
                    if (categories != null) {
                        Arrays.stream(categories)
                                .filter(category -> category.getId() != null)
                                .forEach(category -> categoryStore.save(category.getId().toString(), category));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке категорий: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadLocations() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(locationsUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    Location[] locations = objectMapper.readValue(response.getEntity().getContent(), Location[].class);
                    if (locations != null) {
                        for (Location location : locations) {
                            location.setId(String.valueOf(currentLocationId));
                            currentLocationId++;
                            locationStore.save(location.getId(), location);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке локаций: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
