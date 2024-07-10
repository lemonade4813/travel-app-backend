package com.example.travelappbackend.entity.hotel;


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
    private Boolean available;
    private String category;
    private String roomType;
    private Integer beds;
    private String bedType;
    private String currency;
    private String base;
    private String total;
    private String rate;

}
