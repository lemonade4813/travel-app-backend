package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.flight.FlightDetailInfo;
import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.repository.flight.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.flight.FlightInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FlightService {

    @Autowired
    FlightInfoRepository flightInfoRepository;

    @Autowired
    FlightDetailInfoRepository flightDetailInfoRepository;

    public List<FlightInfo> getFlightList(){
        return flightInfoRepository.findAll();
    }

    public FlightDetailInfo getFlightDetailInfo(int offerId){
        return flightDetailInfoRepository.findByOfferId(offerId);
    }




}
