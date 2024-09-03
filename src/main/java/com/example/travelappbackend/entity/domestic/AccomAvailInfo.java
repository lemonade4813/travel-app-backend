package com.example.travelappbackend.entity.domestic;

import lombok.Data;

@Data
public class AccomAvailInfo {
    private Integer aTypeAvailCount;
    private Integer aTypePrice;
    private Integer bTypeAvailCount;
    private Integer bTypePrice;
    private String checkInDate;
}
