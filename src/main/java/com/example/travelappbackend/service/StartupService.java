package com.example.travelappbackend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
        runScheduledFlightCollectTask(); // 시작시 한 번 실행
    }

    @Scheduled(cron = "0 0 12 * * ?") // 매일 오후 12시에 실행
    public void runScheduledFlightCollectTask() {
        String[] locationCodes = {"ICN", "SYD", "JFK"};

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

    @Scheduled(cron = "0 0 13 * * ?") // 매일 오후 12시에 실행
    public void runScheduledHotelListCollectTask() {
        String[] locationCodes = {"NYC", "PAR", "TYO", "SEL", "BJS"};

        // 현재 날짜 기준으로 2일 뒤의 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate departureDate = today.plusDays(2);
        String departureDateString = departureDate.toString();

//        int adults = 1;
//        boolean nonStop = false;
//        int max = 20;
//
//        // Iterate over all combinations of origin and destination
//        for (String origin : locationCodes) {
//            for (String destination : locationCodes) {
//                if (origin.contains("ICN") || destination.contains("ICN")) {
//                    if (!origin.equals(destination)) {
//                        String url = buildFlightOfferUrl(origin, destination, departureDateString, adults, nonStop, max);
//                        System.out.println("Fetching data for URL: " + url);
//                        amadeusFlightService.fetchData(url);
//                    }
//                }
//            }
//        }
    }

    @Scheduled(cron = "0 0 14 * * ?") // 매일 오후 12시에 실행
    public void runScheduledHotelDetailInfoCollectTask() {
//        String[] locationCodes = {"NYC", "PAR", "TYO", "SEL", "BJS"};

        // 현재 날짜 기준으로 2일 뒤의 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate departureDate = today.plusDays(2);
        String departureDateString = departureDate.toString();

//        int adults = 1;
//        boolean nonStop = false;
//        int max = 20;
//
//        // Iterate over all combinations of origin and destination
//        for (String origin : locationCodes) {
//            for (String destination : locationCodes) {
//                if (origin.contains("ICN") || destination.contains("ICN")) {
//                    if (!origin.equals(destination)) {
//                        String url = buildFlightOfferUrl(origin, destination, departureDateString, adults, nonStop, max);
//                        System.out.println("Fetching data for URL: " + url);
//                        amadeusFlightService.fetchData(url);
//                    }
//                }
//            }
//        }
    }


    private String buildFlightOfferUrl(String origin, String destination, String departureDate, int adults, boolean nonStop, int max) {
        return String.format(
                "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=%d&nonStop=%b&max=%d",
                origin, destination, departureDate, adults, nonStop, max
        );
    }

    private String buildHotelListUrl(String cityCode, int radius) {
        return String.format(
                "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=%s&radius=%d&radiusUnit=KM&hotelSource=ALL",
               cityCode, radius
        );
    }

    private String buildHotelDetailInfoUrl(String[] hotelIds, int adults, String checkInDate, int roomQuantity) {
        return String.format(
                "https://test.api.amadeus.com/v3/shopping/hotel-offers?hotelIds=%d,string&adults=%s&checkInDate=%s&roomQuantity=%d&paymentPolicy=NONE&bestRateOnly=true",
                hotelIds, adults, checkInDate, roomQuantity
        );
    }


}