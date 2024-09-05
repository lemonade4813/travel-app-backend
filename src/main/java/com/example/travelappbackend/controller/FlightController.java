package com.example.travelappbackend.controller;


import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getFlightList() {
        try {
            List<FlightInfo> flightList = flightService.getFlightList();
            ResponseDTO<List<FlightInfo>> response = ResponseDTO.<List<FlightInfo>>builder().data(flightList).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
