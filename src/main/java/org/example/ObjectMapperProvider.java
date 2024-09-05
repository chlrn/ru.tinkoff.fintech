package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperProvider() {
        // Приватный конструктор для предотвращения создания экземпляров
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }
}
