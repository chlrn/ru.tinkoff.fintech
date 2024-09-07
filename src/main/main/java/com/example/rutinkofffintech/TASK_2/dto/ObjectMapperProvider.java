package com.example.rutinkofffintech.TASK_2.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Настройки, если требуется
    static {
        // objectMapper.registerModule(...); // Подключение нужных модулей
    }

    private ObjectMapperProvider() {
        // Приватный конструктор для предотвращения создания экземпляров
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }
}