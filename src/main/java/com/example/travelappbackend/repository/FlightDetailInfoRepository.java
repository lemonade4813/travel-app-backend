package com.example.travelappbackend.repository;


import com.example.travelappbackend.entity.FlightDetailInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDetailInfoRepository extends MongoRepository<FlightDetailInfo, String> {

}
