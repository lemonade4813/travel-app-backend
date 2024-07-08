package com.example.travelappbackend.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Address {
    private String countryCode;
}
