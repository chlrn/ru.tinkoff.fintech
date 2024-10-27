package com.example.rutinkofffintech.task_10.repository;

import com.example.rutinkofffintech.task_10.dto.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}


