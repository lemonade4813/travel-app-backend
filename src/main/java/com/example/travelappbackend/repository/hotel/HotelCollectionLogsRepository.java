package com.example.travelappbackend.repository.hotel;

import com.example.travelappbackend.entity.logs.HotelCollectionLogs;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Optional;

public interface HotelCollectionLogsRepository extends MongoRepository<HotelCollectionLogs, String> {
    Optional<HotelCollectionLogs> findByCollectionDateAndSaveSuccess(String collectionDate, Boolean saveSuccess);
}
