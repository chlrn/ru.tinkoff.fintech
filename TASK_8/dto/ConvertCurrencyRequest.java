package com.example.rutinkofffintech.TASK_8.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor

public class ConvertCurrencyRequest {

    @NotBlank(message = "From currency is required")
    private String fromCurrency;

    @NotBlank(message = "To currency is required")
    private String toCurrency;

    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

}

