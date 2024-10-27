package com.example.rutinkofffintech.TASK_10.specifications;

import com.example.rutinkofffintech.TASK_10.dto.Event;
import com.example.rutinkofffintech.TASK_10.dto.Place;
import org.springframework.data.jpa.domain.Specification;

import java.security.Timestamp;

public class EventSpecifications {
    public static Specification<Event> hasName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Event> hasPlace(Place place) {
        return (root, query, cb) -> cb.equal(root.get("place"), place);
    }

    public static Specification<Event> betweenDates(Timestamp fromDate, Timestamp toDate) {
        return (root, query, cb) -> cb.between(root.get("date"), fromDate, toDate);
    }


}

