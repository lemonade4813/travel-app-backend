package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.service.DomesticAccomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DomesticAccomController {

    @Autowired
    DomesticAccomService domesticAccomService;

    @GetMapping("/domestic/accom")
    public ResponseEntity<?> getDomesticAccomList() {
        try {
            List<Accom> domesticAccomList = domesticAccomService.getDomesticAccom();
            return ResponseEntity.ok().body(domesticAccomList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("500 에러가 발생했습니다.");
        }


    }

}
