package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.entity.domestic.AccomDetail;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.PurchaseAccomItemDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.DomesticAccomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DomesticAccomController {

    @Autowired
    DomesticAccomService domesticAccomService;

    @GetMapping("/domestic/accom")
    public ResponseEntity<?> getDomesticAccomList() {
        try {
            List<Accom> domesticAccomList = domesticAccomService.getDomesticAccom();
            ResponseDTO<List<Accom>> response = ResponseDTO.<List<Accom>>builder().data(domesticAccomList).build();
            return ResponseEntity.ok().body(response);
//            ErrorDTO error = ErrorDTO.builder().error("에러가 발생하였습니다.").build();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/domestic/accom/detail/{contentId}")
    public ResponseEntity<?> getDomesitcAccomDetailInfo(@PathVariable String contentId){
        try{
            AccomDetail domesticAccomDetailInfo = domesticAccomService.getDomesticAccomDetailInfo(contentId);
            ResponseDTO<AccomDetail> response = ResponseDTO.<AccomDetail>builder().data(domesticAccomDetailInfo).build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/domestic/accom/purchase")
    public ResponseEntity<?> purchaseAccomItem(@RequestBody PurchaseAccomItemDTO purchaseAccomItemDTO) {
        try {
            domesticAccomService.conductAccomPurchase(purchaseAccomItemDTO);
            ResponseDTO message = ResponseDTO.builder().message("정상적으로 예약이 완료되었습니다.").build();
            return ResponseEntity.ok().body(message);
        } catch (RuntimeException e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
