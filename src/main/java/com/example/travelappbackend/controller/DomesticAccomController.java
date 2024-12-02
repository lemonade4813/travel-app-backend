package com.example.travelappbackend.controller;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.entity.domestic.AccomDetail;
import com.example.travelappbackend.entity.domestic.PurchaseAccomItem;
import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.PurchaseAccomItemDTO;
import com.example.travelappbackend.model.ResponseDTO;
import com.example.travelappbackend.service.DomesticAccomService;
import com.example.travelappbackend.service.StartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            e.getStackTrace();
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

    @PostMapping("/domestic/accom/reservations")
    public ResponseEntity<?> reserveAccomItem(@RequestBody PurchaseAccomItemDTO purchaseAccomItemDTO, @AuthenticationPrincipal String userId) {
        try {
            purchaseAccomItemDTO.setUserId(userId);
            domesticAccomService.conductAccomPurchase(purchaseAccomItemDTO);
            ResponseDTO message = ResponseDTO.builder().message("정상적으로 예약이 완료되었습니다.").build();
            return ResponseEntity.ok().body(message);
        } catch (RuntimeException e) {

            e.getStackTrace();
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {

            e.getStackTrace();
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/domestic/accom/reservations")
    public ResponseEntity<?> purchaseAccomInfo(@AuthenticationPrincipal String userId) {
        try {
            List <PurchaseAccomItem> purchaseAccomItems = domesticAccomService.getAccomPurchaseInfo(userId);
            ResponseDTO response = ResponseDTO.<List<PurchaseAccomItem>>builder().data(purchaseAccomItems).build();
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            e.getStackTrace();
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            e.getStackTrace();
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }


    @DeleteMapping("/domestic/accom/reservations")
    public ResponseEntity<?> deleteAccomPurchase(@RequestParam String contentid,
                                                 @RequestParam String itemId,
                                                 @RequestParam String purchaseId
                                                 ){
        System.out.println(contentid);
        System.out.println(itemId);
        System.out.println(purchaseId);

        try{
            String message = domesticAccomService.deleteAccomPurchase(contentid, itemId, purchaseId);
            ResponseDTO response = ResponseDTO.builder().data(message).build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
            System.out.println(error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
