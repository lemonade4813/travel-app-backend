package com.example.travelappbackend.entity.hotel;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Distance {
    public Float value;
    public String unit;
}
