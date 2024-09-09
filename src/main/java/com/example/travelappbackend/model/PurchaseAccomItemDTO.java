package com.example.travelappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PurchaseAccomItemDTO {
    private String contentid;
    private String itemId;
    private Integer price;
    private String title;
    private String type;
}
