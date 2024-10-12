package com.example.rutinkofffintech.TASK_5.controllers;

import com.example.rutinkofffintech.TASK_5.dto.Event;
import com.example.rutinkofffintech.TASK_5.init.CurrencyConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {


    private RestTemplate restTemplate;

    // Метод для получения событий из внешнего API
    private List<Event> fetchEvents(String dateFrom, String dateTo) {
        String apiUrl = String.format("https://kudago.com/public-api/v1.4/events/?actual_since=%s&actual_until=%s", dateFrom, dateTo);
        Event[] events = restTemplate.getForObject(apiUrl, Event[].class);
        return events != null ? Arrays.asList(events) : new ArrayList<>();
    }

    // Метод для конвертации бюджета
    private double convertBudget(double budget, String currency) {
        if ("rub".equalsIgnoreCase(currency)) {
            return budget; // Если уже в рублях, возвращаем без изменений
        }

        // Пример запроса к API для получения курса валют
        String conversionApiUrl = String.format("https://api.exchangerate-api.com/v4/latest/%s", currency);
        CurrencyConverter.CurrencyResponse currencyResponse = restTemplate.getForObject(conversionApiUrl, CurrencyConverter.CurrencyResponse.class);

        if (currencyResponse != null && currencyResponse.getRates().containsKey("RUB")) {
            double conversionRate = currencyResponse.getRates().get("RUB");
            return budget * conversionRate; // Конвертация в рубли
        }

        return budget; // Если не удалось получить курс, возвращаем оригинальный бюджет
    }

    // Метод для фильтрации событий по бюджету
    private List<Event> filterEventsByBudget(List<Event> events, double budget) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getPrice() <= budget) { // Предполагаем, что у события есть поле price
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    // Метод для получения начала недели
    private String getStartOfWeek() {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        return startOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE); // Возвращает дату в формате YYYY-MM-DD
    }

    // Метод для получения конца недели
    private String getEndOfWeek() {
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        return endOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE); // Возвращает дату в формате YYYY-MM-DD
    }
}
