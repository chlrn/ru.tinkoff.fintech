package com.example.rutinkofffintech.TASK_5.init;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CurrencyConverter {

    private final String apiUrl = "https://api.exchangerate-api.com/v4/latest/RUB";

    public double convertToRubles(double amount, String currency) {
        RestTemplate restTemplate = new RestTemplate();

        // Получаем данные с API
        CurrencyResponse response = restTemplate.getForObject(apiUrl, CurrencyResponse.class);

        if (response == null || response.getRates() == null) {
            throw new RuntimeException("Ошибка получения данных валюты");
        }

        // Получаем курс валюты
        Double rate = response.getRates().get(currency.toUpperCase());

        if (rate == null) {
            throw new IllegalArgumentException("Неподдерживаемая валюта: " + currency);
        }

        // Конвертация в рубли
        return amount * rate;
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
