package com.example.travelappbackend.repository.hotel;
import com.example.travelappbackend.entity.hotel.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelInfoRepository extends MongoRepository<Hotel, String> {
}
