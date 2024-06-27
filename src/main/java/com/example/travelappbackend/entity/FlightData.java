package com.example.travelappbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "flight_data")
public class FlightData {

    @Id
    private Long Id;

    private String type;
    private boolean oneWay;
    private String lastTicketingDate;
    private String originLocationCode;
    private String destinationLocationCode;
    private Integer offerId;
    private String currency;
    private String total;
    private String base;


}
