package com.example.travelappbackend.controller;


import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HotelController {

    @Autowired
    HotelService hotelService;

    @GetMapping("/hotel")
    public ResponseEntity<?> getFlightList() {
        try {
            List<Hotel> hotelList= hotelService.getHotelList();
            ResponseDTO<List<Hotel>> response = ResponseDTO.<List<Hotel>>builder().data(hotelList).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getFlightList(@PathVariable String hotelId) {
        try {
            HotelDetailInfo hotelDetailInfo = hotelService.getHotelDetailInfo(hotelId);
            ResponseDTO<HotelDetailInfo> response = ResponseDTO.<HotelDetailInfo>builder().data(hotelDetailInfo).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }




}
