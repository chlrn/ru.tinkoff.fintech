package com.example.rutinkofffintech.TASK_8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class ConvertCurrencyResponse {
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;


}
