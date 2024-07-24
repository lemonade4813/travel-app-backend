package com.example.travelappbackend.controller;


import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HotelController {

    @Autowired
    HotelService hotelService;



    @GetMapping("/hotel")
    public ResponseEntity<List<Hotel>> getFlightList() {
        try {
            List<Hotel> hotelList= hotelService.getHotelList();
            return ResponseEntity.ok(hotelList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<HotelDetailInfo> getFlightList(@PathVariable String hotelId) {
        try {
            HotelDetailInfo hotelDetailInfo = hotelService.getHotelDetailInfo(hotelId);
            return ResponseEntity.ok(hotelDetailInfo);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }




}
