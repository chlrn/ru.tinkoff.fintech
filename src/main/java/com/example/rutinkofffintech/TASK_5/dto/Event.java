package com.example.rutinkofffintech.TASK_5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String id;
    private String name;
    private String date; // Формат даты может быть настроен
    private double price; // Цена события
}
