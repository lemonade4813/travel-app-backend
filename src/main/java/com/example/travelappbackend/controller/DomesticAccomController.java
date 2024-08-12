package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.service.DomesticAccomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/domestic/accom/detail/{contentId}")
    public ResponseEntity<?> getDomesitcAccomDetailInfo(@PathVariable String contentId){
        try{
            Accom domesticAccomDetailInfo = domesticAccomService.getDomesticAccomDetailInfo(contentId);
            return ResponseEntity.ok().body(domesticAccomDetailInfo);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
