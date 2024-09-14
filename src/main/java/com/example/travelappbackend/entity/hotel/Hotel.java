package com.example.travelappbackend.entity.hotel;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document

public class Hotel {

    @Id
    private String id;

    private String name;
    private String hotelId;
    private String distance;
    private String unit;
    private String cityCode;

    private Double latitude;
    private Double longitude;
//    private Geocode geocode;

    private String countryCode;

}
