package com.example.travelappbackend.service;


import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.repository.hotel.HotelInfoRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HotelService {


    @Autowired
    HotelInfoRepository hotelInfoRepository;

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

    public void createPurchase(String hotelId, String offerId) {

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
                            .append("purchaseDate", new java.util.Date());

                    mongoTemplate.insert(purchase, "purchases_hotel_info");
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
}
