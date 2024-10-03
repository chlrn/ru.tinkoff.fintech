package com.example.rutinkofffintech.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Генерирует конструктор с параметрами для всех полей
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Category {
    private String id;
    private String name;
}
