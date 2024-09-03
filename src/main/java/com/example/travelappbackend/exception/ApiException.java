package com.example.travelappbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {
    private int code;
    private String title;
    private String detail;
    private int status;

    public ApiException(String message) {
        super(message);
    }
}