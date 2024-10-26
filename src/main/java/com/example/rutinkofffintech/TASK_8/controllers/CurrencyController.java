package com.example.rutinkofffintech.TASK_8.controllers;

import com.example.rutinkofffintech.TASK_8.dto.ConvertCurrencyRequest;
import com.example.rutinkofffintech.TASK_8.dto.ConvertCurrencyResponse;
import com.example.rutinkofffintech.TASK_8.dto.CurrencyRateResponse;
import com.example.rutinkofffintech.TASK_8.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Validated
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/rates/{code}")
    public ResponseEntity<CurrencyRateResponse> getCurrencyRate(@PathVariable String code) {
        Double rate = currencyService.getCurrencyRate(code);
        return ResponseEntity.ok(new CurrencyRateResponse(code, rate));
    }

    @PostMapping("/convert")
    public ResponseEntity<ConvertCurrencyResponse> convertCurrency(@Valid @RequestBody ConvertCurrencyRequest request) {
        double convertedAmount = currencyService.convertCurrency(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount());
        return ResponseEntity.ok(new ConvertCurrencyResponse(
                request.getFromCurrency(),
                request.getToCurrency(),
                convertedAmount
        ));
    }
}
