package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.service.FlightInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightInfoController {

    @Autowired
    private FlightInfoService flightInfoService;

    @GetMapping("/flight/offer")
    public ResponseEntity<?> flightOffers() {
        try {
            List<FlightInfo> flightInfos = flightInfoService.getFlightInfo();
            return ResponseEntity.ok(flightInfos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("항공편 데이터 조회에 실패했습니다.");
        }
    }

    @GetMapping("/flight/offer/{id}")
    public ResponseEntity<?> flightOfferDetailInfo(@PathVariable String id){
        try{
            FlightInfo flightInfo = flightInfoService.getFlightItem(id);
            return ResponseEntity.ok(flightInfo);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("항공편 상세 정보 조회에 실패했습니다.");
        }
    }
}
