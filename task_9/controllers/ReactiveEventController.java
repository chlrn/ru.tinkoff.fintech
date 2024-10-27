package com.example.rutinkofffintech.task_9.controllers;

import com.example.rutinkofffintech.task_9.dto.Event;
import com.example.rutinkofffintech.task_9.init.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1/events")
public class ReactiveEventController {

    private final WebClient webClient;
    private final Semaphore semaphore;

    @Autowired
    public ReactiveEventController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kudago.com").build();
        this.semaphore = new Semaphore(5); // Максимальное количество одновременных запросов
    }

    @GetMapping
    public Flux<Event> getEvents() {
        String dateFrom = getStartOfWeek();
        String dateTo = getEndOfWeek();

        return fetchEvents(dateFrom, dateTo)
                .flatMap(this::convertEvents);
    }

    // Метод для получения событий из внешнего API
    private Flux<Event> fetchEvents(String dateFrom, String dateTo) {
        return Mono.just(String.format("/public-api/v1.4/events/?actual_since=%s&actual_until=%s", dateFrom, dateTo))
                .flatMapMany(url -> {
                    try {
                        semaphore.acquire();
                        return webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToFlux(Event.class)
                                .doFinally(signalType -> semaphore.release());
                    } catch (InterruptedException e) {
                        return Flux.error(e);
                    }
                });
    }

    // Метод для конвертации событий с учетом бюджета
    private Flux<Event> convertEvents(Event event) {
        return Mono.just(event)
                .flatMap(evt -> {
                    double convertedBudget = convertBudget(evt.getPrice(), "usd");
                    evt.setPrice(convertedBudget); // Изменяем цену
                    return Mono.just(evt);
                })
                .flux();
    }

    // Метод для конвертации бюджета
    private double convertBudget(double budget, String currency) {
        if ("rub".equalsIgnoreCase(currency)) {
            return budget; // Если уже в рублях, возвращаем без изменений
        }

        String conversionApiUrl = String.format("https://api.exchangerate-api.com/v4/latest/%s", currency);
        CurrencyConverter.CurrencyResponse currencyResponse = webClient.get()
                .uri(conversionApiUrl)
                .retrieve()
                .bodyToMono(CurrencyConverter.CurrencyResponse.class)
                .block(); // Здесь блокируем только для получения курса валют

        if (currencyResponse != null && currencyResponse.getRates().containsKey("RUB")) {
            double conversionRate = currencyResponse.getRates().get("RUB");
            return budget * conversionRate; // Конвертация в рубли
        }

        return budget; // Если не удалось получить курс, возвращаем оригинальный бюджет
    }

    // Метод для получения начала недели
    private String getStartOfWeek() {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        return startOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Метод для получения конца недели
    private String getEndOfWeek() {
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        return endOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
