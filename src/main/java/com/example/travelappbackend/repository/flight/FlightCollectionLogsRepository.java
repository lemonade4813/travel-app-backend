package com.example.travelappbackend.repository.flight;

import com.example.travelappbackend.entity.logs.FlightCollectionLogs;
import com.example.travelappbackend.entity.logs.HotelCollectionLogs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightCollectionLogsRepository extends MongoRepository<FlightCollectionLogs, String> {
    Optional<HotelCollectionLogs> findByCollectionDateAndSaveSuccess(String collectionDate, Boolean saveSuccess);
}
