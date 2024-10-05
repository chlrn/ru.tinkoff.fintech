package com.example.rutinkofffintech.TASK_8.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private int code;
    private String message;

}
