package com.example.rutinkofffintech.task_9.init;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/RUB";

    public BigDecimal convertToRubles(BigDecimal amount, String currency) {
        RestTemplate restTemplate = new RestTemplate();

        // Получаем данные с API
        CurrencyResponse response = restTemplate.getForObject(API_URL, CurrencyResponse.class);

        if (response == null || response.getRates() == null) {
            throw new RuntimeException("Ошибка получения данных валюты");
        }

        // Получаем курс валюты
        Double rate = response.getRates().get(currency.toUpperCase());

        if (rate == null) {
            throw new IllegalArgumentException("Неподдерживаемая валюта: " + currency);
        }

        // Конвертация в рубли
        return amount.multiply(BigDecimal.valueOf(rate));
    }

    // Вспомогательный класс для парсинга ответа от API
    public static class CurrencyResponse {
        private String base;
        private Map<String, Double> rates;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }
    }
}
