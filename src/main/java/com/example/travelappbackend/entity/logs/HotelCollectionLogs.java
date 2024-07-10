package com.example.travelappbackend.entity.logs;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "hotel_collection_logs")
public class HotelCollectionLogs{

    @Id
    private String id;
    private String collectionDate;
    private Boolean saveSuccess;
}
