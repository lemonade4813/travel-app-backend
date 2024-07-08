package com.example.travelappbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Arrival {
    private String iataCode;
    private String terminal;
    private String at;
}