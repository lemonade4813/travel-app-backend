package com.example.travelappbackend.entity.domestic;

import lombok.Data;

import java.util.UUID;

@Data
public class AccomAvailInfo {
    private String itemId;
    private String type;
    private Integer availCount;
    private Integer price;
    private String checkInDate;

    public AccomAvailInfo(){
        this.itemId = UUID.randomUUID().toString();
    }
}
