package com.example.travelappbackend.entity;


import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class HotelDetailInfo {

    @Id

    private String id;
    private String hotelId;
    private String chainCode;
    private String name;
    private String offersId;
    private String checkInDate;
    private String checkOutDate;




}
