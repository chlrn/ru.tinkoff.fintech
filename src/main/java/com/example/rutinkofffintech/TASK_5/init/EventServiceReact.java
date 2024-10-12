package com.example.rutinkofffintech.TASK_5.init;

import com.example.rutinkofffintech.TASK_5.dto.Event;
import com.example.rutinkofffintech.TASK_5.repository.EventRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceReact {

    private final EventRepository eventRepository;
    private final CurrencyConverter currencyConverter;

    public EventServiceReact(EventRepository eventRepository, CurrencyConverter currencyConverter) {
        this.eventRepository = eventRepository;
        this.currencyConverter = currencyConverter;
    }

    public Mono<List<Event>> getEvents(double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        Mono<List<com.example.rutinkofffintech.TASK_5.dto.Event>> eventsMono = Mono.fromCallable(() ->
                eventRepository.getEvents(dateFrom, dateTo)
        );

        Mono<Double> convertedBudgetMono = Mono.fromCallable(() ->
                currencyConverter.convertToRubles(budget, currency)
        );

        return Mono.zip(eventsMono, convertedBudgetMono)
                .map(tuple -> {
                    List<Event> events = tuple.getT1();
                    Double convertedBudget = tuple.getT2();
                    return events.stream()
                            .filter(event -> event.getPrice() <= convertedBudget)
                            .collect(Collectors.toList());
                });
    }
}
