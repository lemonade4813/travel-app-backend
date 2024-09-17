package com.example.travelappbackend.entity.hotel;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("purchase_hotel_info")
@Data
public class PurchaseHotelItem {

    @Id
    private String purchaseId;
    private String hotelId;
    private String offerId;
    private Date purchaseDate;
}
