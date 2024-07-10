package com.example.travelappbackend.repository.flight;

import com.example.travelappbackend.entity.flight.FlightInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightInfoRepository extends MongoRepository<FlightInfo, String> {

}
