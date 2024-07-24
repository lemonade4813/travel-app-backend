package com.example.travelappbackend.repository.hotel;

import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelDetailInfoRepository extends MongoRepository<HotelDetailInfo, String> {

    public HotelDetailInfo findHotelDetailInfoByHotelId(String hotelId);

}
