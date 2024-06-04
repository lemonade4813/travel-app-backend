package com.example.travelappbackend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartupService {

    private final AmadeusFlightService amadeusFlightService;

    @Autowired
    public StartupService(AmadeusFlightService amadeusFlightService) {
        this.amadeusFlightService = amadeusFlightService;
    }

    @PostConstruct
    public void init() {
        String url = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2024-06-10&adults=1&nonStop=false&max=250";

        System.out.println("함수 실행");
        amadeusFlightService.fetchData(url);
    }
}