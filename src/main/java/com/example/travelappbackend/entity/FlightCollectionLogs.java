package com.example.travelappbackend.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "flight_collection_logs")

public class FlightCollectionLogs {

    @Id
    private String id;
    private Date collectionDate;
    private Boolean saveSuccess;

}



