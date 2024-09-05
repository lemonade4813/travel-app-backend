package com.example.travelappbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO<T> {
    private String message;
    private T data;
}
