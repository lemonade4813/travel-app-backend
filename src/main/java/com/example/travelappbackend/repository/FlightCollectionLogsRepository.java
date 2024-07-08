package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.FlightCollectionLogs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightCollectionLogsRepository extends MongoRepository<FlightCollectionLogs, String> {
}
