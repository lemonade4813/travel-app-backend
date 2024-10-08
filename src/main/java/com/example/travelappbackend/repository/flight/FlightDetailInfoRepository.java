package com.example.travelappbackend.repository.flight;


import com.example.travelappbackend.entity.flight.FlightDetailInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightDetailInfoRepository extends MongoRepository<FlightDetailInfo, String> {

    FlightDetailInfo findByOfferId(int offerId);
}
