package com.example.travelappbackend.controller;


import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.PurchaseHotelItemDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.HotelService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HotelController {

    @Autowired
    HotelService hotelService;

    @GetMapping("/hotel")
    public ResponseEntity<?> getHotelList() {
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
    public ResponseEntity<?> getHotelDetailInfo(@PathVariable String hotelId) {

        try {
            Document hotelDetailInfo = hotelService.getHotelDetailInfo(hotelId);
            System.out.println(hotelDetailInfo);
            ResponseDTO<Document> response = ResponseDTO.<Document>builder().data(hotelDetailInfo).build();
            System.out.println(response);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/hotel/purchase")
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseHotelItemDTO purchaseHotelItemDTO) {
        try {
            System.out.println("1222222222222222");
            System.out.println(purchaseHotelItemDTO.getHotelId() + "  "  + purchaseHotelItemDTO.getOfferId());
            hotelService.createPurchase(purchaseHotelItemDTO.getHotelId(), purchaseHotelItemDTO.getOfferId());
            ResponseDTO<String> response = ResponseDTO.<String>builder().data("결제가 성공적으로 처리되었습니다.").build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error("결제 처리 중 오류가 발생했습니다: " + e.getMessage()).build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


}
