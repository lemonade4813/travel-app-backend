package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.flight.FlightDetailInfo;
import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.repository.flight.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.flight.FlightInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.bson.Document;

import java.util.List;
import java.util.Optional;


@Service
public class FlightService {

    @Autowired
    FlightInfoRepository flightInfoRepository;

    @Autowired
    FlightDetailInfoRepository flightDetailInfoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public List<FlightInfo> getFlightList(){
        return flightInfoRepository.findAll();
    }

    public Document getFlightDetailInfo(int offerId){
//        return flightDetailInfoRepository.findByOfferId(offerId);
        Query query = new Query();
        query.addCriteria(Criteria.where("offerId").is(offerId));
        return mongoTemplate.findOne(query, Document.class, "flight_detail_info");


    }




}
