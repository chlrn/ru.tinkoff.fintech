package com.example.rutinkofffintech.TASK_5.repository;

import com.example.rutinkofffintech.TASK_5.dto.Event;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository {
    List<Event> getEvents(LocalDate dateFrom, LocalDate dateTo);
}
