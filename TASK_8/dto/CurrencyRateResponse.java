package com.example.rutinkofffintech.TASK_8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor

public class CurrencyRateResponse {

    private String currency;
    private BigDecimal rate;

}

