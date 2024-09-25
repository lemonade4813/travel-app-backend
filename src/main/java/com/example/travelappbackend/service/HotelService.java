package com.example.travelappbackend.service;


import com.example.travelappbackend.entity.domestic.AccomAvailInfo;
import com.example.travelappbackend.entity.domestic.AccomDetail;
import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.entity.hotel.PurchaseHotelItem;
import com.example.travelappbackend.repository.hotel.HotelDetailInfoRepository;
import com.example.travelappbackend.repository.hotel.HotelInfoRepository;
import com.example.travelappbackend.repository.hotel.HotelPurchaseItemRepository;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {


    @Autowired
    HotelInfoRepository hotelInfoRepository;

    @Autowired
    HotelDetailInfoRepository hotelDetailInfoRepository;

    @Autowired
    HotelPurchaseItemRepository hotelPurchaseItemRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    public List<Hotel> getHotelList(){
        return hotelInfoRepository.findAll();
    }

    public Document getHotelDetailInfo(String hotelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("hotelId").is(hotelId));
        // Document로 결과를 반환
        return mongoTemplate.findOne(query, Document.class, "hotel_detail_info");
    }

    public void createPurchase(String hotelId, String offerId, String userId) {

        Query hotelQuery = new Query(Criteria.where("hotelId").is(hotelId));

        hotelQuery.addCriteria(Criteria.where("offers.offerId").is(offerId));


        Document hotelDetailInfo = mongoTemplate.findOne(hotelQuery, Document.class, "hotel_detail_info");

        if (hotelDetailInfo != null) {

            Document offerToUpdate = null;
            List<Document> offers = (List<Document>) hotelDetailInfo.get("offers");

            for (Document offer : offers) {
                if (offerId.equals(offer.getString("offerId"))) {
                    offerToUpdate = offer;
                    break;
                }
            }

            System.out.println(offerToUpdate);

            if (offerToUpdate != null && offerToUpdate.getBoolean("available")) {

                Query offerQuery = new Query(Criteria.where("hotelId").is(hotelId)
                        .and("offers.offerId").is(offerId));

                Update update = new Update().set("offers.$.available", false);
                UpdateResult updateResult = mongoTemplate.updateFirst(offerQuery, update, "hotel_detail_info");

                if (updateResult.getMatchedCount() > 0) {

                    Document purchase = new Document()
                            .append("hotelId", hotelId)
                            .append("offerId", offerId)
                            .append("userId", userId)
                            .append("purchaseDate", new java.util.Date());

                    mongoTemplate.insert(purchase, "purchase_hotel_info");
                    System.out.println("예약이 성공적으로 진행되었습니다.");
                } else {
                    throw new RuntimeException("호텔 예약에 실패하였습니다.");
                }
            } else {
                throw new RuntimeException("상품이 조회에 실패하였습니다.");
            }
        } else {
            throw new RuntimeException("호텔 정보 조회에 실패하였습니다.");
        }
    }


    public List<PurchaseHotelItem> getPurchaseList() {
        return hotelPurchaseItemRepository.findAll();
    }

    public String deleteHotelPurchase (String purchaseId, String hotelId, String offerId) {
        try {
            boolean exists = hotelPurchaseItemRepository.existsByPurchaseId(purchaseId);

            System.out.println("exists : " + exists);
            if(exists) {
                hotelPurchaseItemRepository.deleteByPurchaseId(purchaseId);
                updateAvailCount(hotelId, offerId);

            }
        } catch (Exception e){
            throw new RuntimeException("예약 삭제에 실패하였습니다.");
        }
        return "정상적으로 삭제가 완료되었습니다.";
    }


    public boolean updateAvailCount(String hotelId, String offerId) {
        // hotelId에 해당하는 호텔을 검색
        Query query = new Query(Criteria.where("hotelId").is(hotelId)
                .and("offers.offerId").is(offerId));

        // offers 배열 안의 특정 offerId를 가진 객체의 available 속성을 true로 변경
        Update update = new Update().set("offers.$.available", true);

        // 업데이트 실행
        Document result = mongoTemplate.findAndModify(query, update, Document.class, "hotel_detail_info");

        // 업데이트가 성공했는지 확인
        return result != null;
    }
}
