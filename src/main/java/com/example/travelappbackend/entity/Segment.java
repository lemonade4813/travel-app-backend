package com.example.travelappbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Segment {
    private Departure departure;
    private Arrival arrival;
    private String carrierCode;
    private String number;
    private Aircraft aircraft;
    private Operating operating;
    private String duration;
    private String id;
    private int numberOfStops;
    private boolean blacklistedInEU;
}