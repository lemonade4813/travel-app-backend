package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.CommonService;
import com.example.travelappbackend.service.FlightService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

            List<FlightInfo> flightInfos = commonService.getDataByQueryParams(queryParams, FlightInfo.class);
            ResponseDTO<List<FlightInfo>> response = ResponseDTO.<List<FlightInfo>>builder().data(flightInfos).build();
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/flight/offer/detail/{offerId}")
    public ResponseEntity<?> flightOfferDetailInfo(@PathVariable int offerId){
        try{

            Document flightDetailInfo = flightService.getFlightDetailInfo(offerId);
            ResponseDTO<Document> response = ResponseDTO.<Document>builder().data(flightDetailInfo).build();
            return ResponseEntity.ok().body(response);

//            System.out.println(offerId);
//
//            FlightDetailInfo flightDetailInfo= flightService.getFlightDetailInfo(offerId);

//            System.out.println(flightDetailInfo);
//            return ResponseEntity.ok(flightDetailInfo);
        }
        catch (Exception e){
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
