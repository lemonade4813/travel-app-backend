package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.flight.FlightDetailInfo;
import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.service.CommonService;
import com.example.travelappbackend.service.FlightInfoService;
import com.example.travelappbackend.service.FlightService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FlightInfoController {

    @Autowired
    private FlightService flightService;
    @Autowired
    private CommonService commonService;

    @GetMapping("/flight/offer")
    public ResponseEntity<?> flightOffers(@RequestParam(required = false) String departAirport,
                                          @RequestParam(required = false) String arriveAirport,
                                          @RequestParam(required = false) String departureDate
                                          ) {
        try {

            Map<String, Object> queryParams = new HashMap<>();
//            queryParams.put("departAirport", departAirport);
//            queryParams.put("arriveAirport", arriveAirport);
//            queryParams.put("departureDate", departureDate);


            List <FlightInfo> flightInfos = commonService.getDataByQueryParams(queryParams, FlightInfo.class);

            System.out.println(flightInfos);

            return ResponseEntity.ok(flightInfos);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("항공편 데이터 조회에 실패했습니다.");
        }
    }

    @GetMapping("/flight/offer/detail/{offerId}")
    public ResponseEntity<?> flightOfferDetailInfo(@PathVariable int offerId){
        try{

            Document flightDetailInfo = flightService.getFlightDetailInfo(offerId);


            System.out.println(flightDetailInfo);

            return ResponseEntity.ok(flightDetailInfo);

//            System.out.println(offerId);
//
//            FlightDetailInfo flightDetailInfo= flightService.getFlightDetailInfo(offerId);

//            System.out.println(flightDetailInfo);
//            return ResponseEntity.ok(flightDetailInfo);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("항공편 상세 정보 조회에 실패했습니다.");
        }
    }
}
