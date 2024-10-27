package com.example.rutinkofffintech.task_9.repository;

import com.example.rutinkofffintech.task_9.dto.Event;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository {
    List<Event> getEvents(LocalDate dateFrom, LocalDate dateTo);
}
