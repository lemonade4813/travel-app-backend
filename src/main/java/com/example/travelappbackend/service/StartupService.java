//package com.example.travelappbackend.service;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StartupService {
//
//    private final AmadeusFlightService amadeusFlightService;
//
//    @Autowired
//    public StartupService(AmadeusFlightService amadeusFlightService) {
//        this.amadeusFlightService = amadeusFlightService;
//    }
//
//    @PostConstruct
//    public void init() {
//        String url = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2024-06-10&adults=1&nonStop=false&max=250";
//
//        System.out.println("함수 실행");
//        amadeusFlightService.fetchData(url);
//    }
//}

package com.example.travelappbackend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StartupService {

    private final AmadeusFlightService amadeusFlightService;

    @Autowired
    public StartupService(AmadeusFlightService amadeusFlightService) {
        this.amadeusFlightService = amadeusFlightService;
    }

    @PostConstruct
    public void init() {

        String[] locationCodes = {"ICN","SYD","JFK"};

        // 현재 날짜 기준으로 2일 뒤의 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate departureDate = today.plusDays(2);
        String departureDateString = departureDate.toString();

        int adults = 1;
        boolean nonStop = false;
        int max = 20;

        // Iterate over all combinations of origin and destination
        for (String origin : locationCodes) {
            for (String destination : locationCodes) {
                if (origin.contains("ICN") || destination.contains("ICN")) {
                    if (!origin.equals(destination)) {
                        String url = buildFlightOfferUrl(origin, destination, departureDateString, adults, nonStop, max);
                        System.out.println("Fetching data for URL: " + url);
                        amadeusFlightService.fetchData(url);
                    }
                }
            }
        }
    }

    private String buildFlightOfferUrl(String origin, String destination, String departureDate, int adults, boolean nonStop, int max) {
        return String.format(
                "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=%d&nonStop=%b&max=%d",
                origin, destination, departureDate, adults, nonStop, max
        );
    }
}