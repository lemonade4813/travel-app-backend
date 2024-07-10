package com.example.travelappbackend.entity.flight;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Operating {
    private String carrierCode;
}