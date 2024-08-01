package com.example.travelappbackend.controller;


import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FlightController {
    @Autowired
    FlightService flightService;

    @GetMapping("/flight")
    public ResponseEntity<List<FlightInfo>> getFlightList() {
        try {
            List<FlightInfo> flightList = flightService.getFlightList();
            return ResponseEntity.ok(flightList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
