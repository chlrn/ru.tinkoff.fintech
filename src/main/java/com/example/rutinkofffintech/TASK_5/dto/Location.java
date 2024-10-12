package com.example.rutinkofffintech.TASK_5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Генерирует конструктор с параметрами для всех полей
@NoArgsConstructor
public class Location {
    private String id;
    private String name;
    private String slug;

}
