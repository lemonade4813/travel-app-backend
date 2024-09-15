package com.example.travelappbackend.service;


import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.repository.hotel.HotelDetailInfoRepository;
import com.example.travelappbackend.repository.hotel.HotelInfoRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

    public List<Document> getHotelDetailInfo(String hotelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("hotel.hotelId").is(hotelId));
        // Document로 결과를 반환
        return mongoTemplate.find(query, Document.class, "hotel_detail_info");
    }
}
