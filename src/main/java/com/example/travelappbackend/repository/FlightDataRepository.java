package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.FlightData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightDataRepository extends JpaRepository<FlightData, Long> {

}
