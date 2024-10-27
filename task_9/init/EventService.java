package com.example.rutinkofffintech.task_9.init;

import com.example.rutinkofffintech.task_9.dto.Event;
import com.example.rutinkofffintech.task_9.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CurrencyConverter currencyConverter;
    private final WebClient.Builder webClientBuilder;

    private static final String EVENT_API_URL = "https://kudago.com/public-api/v1.4/events/?actual_since=%s&actual_until=%s";

    public CompletableFuture<List<Event>> getEvents(double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        CompletableFuture<List<Event>> eventsFuture = CompletableFuture.supplyAsync(() ->
                fetchEvents(dateFrom.toString(), dateTo.toString())
        );

        CompletableFuture<BigDecimal> convertedBudgetFuture = CompletableFuture.supplyAsync(() ->
                currencyConverter.convertToRubles(BigDecimal.valueOf(budget), currency)
        );

        return eventsFuture.thenCombine(convertedBudgetFuture, (events, convertedBudget) ->
                events.stream()
                        .filter(event -> event.getPrice() <= convertedBudget)
                        .collect(Collectors.toList())
        );
    }

    // Метод для получения событий из внешнего API
    private List<Event> fetchEvents(String dateFrom, String dateTo) {
        String apiUrl = String.format(EVENT_API_URL, dateFrom, dateTo);

        // Используем WebClient для выполнения запроса к API
        Event[] events = webClientBuilder.build()
                .get()
                .uri(apiUrl)
                .retrieve()  // Выполняем запрос
                .bodyToMono(Event[].class)  // Получаем тело ответа
                .block();  // Блокируем поток до получения результата

        return events != null ? List.of(events) : List.of();
    }
}
