package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.FlightData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDataRepository extends JpaRepository<FlightData, Long> {

}
