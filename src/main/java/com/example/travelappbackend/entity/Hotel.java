package com.example.travelappbackend.entity;

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
    private String radius;
    private String radiusUnit;
    private String chainCodes;
//    private String[] amenities;
    private String[] ratings;

}
