package com.example.rutinkofffintech;

import com.example.rutinkofffintech.init.DataInitializer;
import com.example.rutinkofffintech.datastore.DataStore;
import com.example.rutinkofffintech.dto.Category;
import com.example.rutinkofffintech.dto.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.WireMockContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
public class DataInitializerIntegrationTest {

    // Поднимаем контейнер с WireMock через Testcontainers
    public static WireMockContainer wiremock = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withExposedPorts(8080);

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private DataStore<Category> categoryStore;

    @Autowired
    private DataStore<Location> locationStore;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        wiremock.start();

        // Настраиваем WireMock сервер с помощью Testcontainers
        configureFor(wiremock.getHost(), wiremock.getMappedPort(8080));

        // Мокаем ответ для категорий
        stubFor(get(urlEqualTo("/categories"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":\"1\",\"name\":\"Category1\"}, {\"id\":\"2\",\"name\":\"Category2\"}]")));

        // Мокаем ответ для локаций
        stubFor(get(urlEqualTo("/locations"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\":\"Location1\",\"slug\":\"location1\"}, {\"name\":\"Location2\",\"slug\":\"location2\"}]")));
    }

    @Test
    public void testLoadCategoriesAndLocations() throws Exception {
        // Указываем URL WireMock контейнера в тестовых свойствах
        String wiremockUrl = "http://" + wiremock.getHost() + ":" + wiremock.getMappedPort(8080);
        dataInitializer.setCategoriesUrl(wiremockUrl + "/categories");
        dataInitializer.setLocationsUrl(wiremockUrl + "/locations");

        // Инициализируем загрузку данных
        dataInitializer.init();

        // Проверяем загрузку категорий
        Category category1 = categoryStore.get("1");
        assertEquals("Category1", category1.getName());

        Category category2 = categoryStore.get("2");
        assertEquals("Category2", category2.getName());

        // Проверяем загрузку локаций
        Location location1 = locationStore.get("1");
        assertEquals("Location1", location1.getName());

        Location location2 = locationStore.get("2");
        assertEquals("Location2", location2.getName());
    }
}
