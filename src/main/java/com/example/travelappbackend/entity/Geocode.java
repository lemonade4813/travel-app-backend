package com.example.travelappbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Geocode {
    private Float latitude;
    private Float longitude;
}
