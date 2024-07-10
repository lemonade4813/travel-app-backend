package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.repository.flight.FlightInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightInfoService {

    @Autowired
    private FlightInfoRepository flightInfoRepository;

    public List<FlightInfo> getFlightInfo(){
        return flightInfoRepository.findAll();
    }

    public FlightInfo getFlightItem(String id){
        return flightInfoRepository.findById(id).orElse(null);
    }

}
