package com.example.travelappbackend.service;


import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.hotel.HotelDetailInfo;
import com.example.travelappbackend.repository.flight.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.hotel.HotelDetailInfoRepository;
import com.example.travelappbackend.repository.hotel.HotelInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {


    @Autowired
    HotelInfoRepository hotelInfoRepository;

    @Autowired
    HotelDetailInfoRepository hotelDetailInfoRepository;

    public HotelDetailInfo getHotelDetailInfo(String hotelId){
        return hotelDetailInfoRepository.findHotelDetailInfoByHotelId(hotelId);

    }

    public List<Hotel> getHotelList(){
        return hotelInfoRepository.findAll();
    }


}
