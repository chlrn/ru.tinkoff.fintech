package com.example.rutinkofffintech.init;

import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import com.example.rutinkofffintech.dto.Location;
import org.example.logexecutiontime.LogExecutionTime;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

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
        Optional<Category[]> categoriesOptional = executeHttpGetRequest(categoriesUrl, Category[].class);
        categoriesOptional.ifPresent(categories -> Arrays.stream(categories)
                .filter(category -> category.getId() != null)
                .forEach(category -> categoryStore.save(category.getId().toString(), category)));
    }

    private void loadLocations() {
        Optional<Location[]> locationsOptional = executeHttpGetRequest(locationsUrl, Location[].class);
        locationsOptional.ifPresent(locations -> {
            for (Location location : locations) {
                location.setId(String.valueOf(currentLocationId));
                currentLocationId++;
                locationStore.save(location.getId(), location);
            }
        });
    }


    private <T> Optional<T> executeHttpGetRequest(String url, Class<T> responseType) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return Optional.ofNullable(objectMapper.readValue(response.getEntity().getContent(), responseType));
                } else {
                    System.err.println("Ошибка при выполнении запроса. Статус: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
