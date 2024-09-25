package com.example.travelappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHotelItemDTO {

    private String userId;
    private String hotelId;
    private String offerId;
}
