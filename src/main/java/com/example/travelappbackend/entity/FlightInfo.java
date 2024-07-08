package com.example.travelappbackend.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "flight_info")
public class FlightInfo {

    @Id
    private Long id;

    private String type;
    private boolean oneWay;
    private String lastTicketingDate;
    private String originLocationCode;
    private String destinationLocationCode;
    private Integer offerId;
    private String currency;
    private String total;
    private String base;
    private Integer numberOfBookableSeats;


}
