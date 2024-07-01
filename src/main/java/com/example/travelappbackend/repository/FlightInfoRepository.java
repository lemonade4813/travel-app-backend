package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.FlightInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightInfoRepository extends MongoRepository<FlightInfo, String> {

}
