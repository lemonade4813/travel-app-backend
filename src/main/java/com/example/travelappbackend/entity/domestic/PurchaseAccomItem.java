package com.example.travelappbackend.entity.domestic;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "purchase_accom_info")
public class PurchaseAccomItem {

    @Id
    private String purchaseId;
    private String userId;
    private String contentid;
    private String itemId;
    private Integer price;
    private String purchaseDate;
}
