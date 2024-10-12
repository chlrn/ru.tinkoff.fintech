package com.example.rutinkofffintech.TASK_5.init;

import com.example.rutinkofffintech.TASK_5.dto.Event;
import com.example.rutinkofffintech.TASK_5.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Data

public class EventService {

    private final EventRepository eventRepository;
    private final CurrencyConverter currencyConverter;

    public EventService(EventRepository eventRepository, CurrencyConverter currencyConverter) {
        this.eventRepository = eventRepository;
        this.currencyConverter = currencyConverter;
    }

    public CompletableFuture<List<Event>> getEvents(double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        CompletableFuture<List<Event>> eventsFuture = CompletableFuture.supplyAsync(() ->
                eventRepository.getEvents(dateFrom, dateTo)
        );

        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() ->
                currencyConverter.convertToRubles(budget, currency)
        );

        return eventsFuture.thenCombine(convertedBudgetFuture, (events, convertedBudget) ->
                events.stream()
                        .filter(event -> event.getPrice() <= convertedBudget)
                        .collect(Collectors.toList())
        );
    }
}
