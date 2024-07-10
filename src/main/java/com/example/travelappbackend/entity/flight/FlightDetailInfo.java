package com.example.travelappbackend.entity.flight;


import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "flight_detail_info")
public class FlightDetailInfo {

    @Id
    private String id;

    private String type;
    private boolean oneWay;
    private String lastTicketingDate;
    private String originLocationCode;
    private String destinationLocationCode;
    private Integer numberOfBookableSeats;
    private Integer offerId;
    private String currency;
    private String total;
    private String base;
    private List<Segment> segments;

}
