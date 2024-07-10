package com.example.travelappbackend.entity.hotel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Hotels {
    private Hotel[] hotels;
}
