package com.example.travelappbackend.service;

import com.mongodb.client.model.Projections;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StartupService {
    
    private final AmadeusFlightService amadeusFlightService;
    private final AmadeusHotelService amadeusHotelService;
    private final MongoTemplate mongoTemplate;
    private final DomesticAccomService domesticAccomService;

    @Autowired
    public StartupService(AmadeusFlightService amadeusFlightService,
                          AmadeusHotelService amadeusHotelService,
                          MongoTemplate mongoTemplate,
                          DomesticAccomService domesticAccomService
                          ) {
        this.amadeusFlightService = amadeusFlightService;
        this.amadeusHotelService = amadeusHotelService;
        this.mongoTemplate = mongoTemplate;
        this.domesticAccomService = domesticAccomService;
    }

    @PostConstruct
    public void init() {

        try {
            // 시작시 한 번 실행
//
            runScheduledFlightCollectTask();
            runScheduledHotelListCollectTask();
            runScheduledHotelDetailInfoCollectTask();
            runScheduledDomesticAccomCollectTask();
        }
        catch (Exception e){
            System.err.println("An error occurred during initialization: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @Scheduled(cron = "0 0 12 * * ?") // 매일 오후 12시에 실행
    public void runScheduledFlightCollectTask() {
        String[] locationCodes = {"ICN", "SYD", "JFK", "LHR", "CDG"};

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
                        System.out.println("Fetching data for URL: 1111111111 " + url);
                        amadeusFlightService.fetchAvailFlightData(url);
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0 13 * * ?") // 매일 오후 12시에 실행
    public void runScheduledHotelListCollectTask() {
        String[] cityCodes = {"LON", "NYC", "PAR", "TYO", "SEL", "PEK"};
        for(String cityCode : cityCodes) {
            String url = buildHotelListUrl(cityCode);
            try {
                amadeusHotelService.fetchAvailHotelData(url);
            } catch (Exception e) {
                // Log the error and continue with the next hotelId
                System.err.println("Error fetching hotel details for hotelId");
                e.printStackTrace();
            }

        }
    }

    @Scheduled(cron = "0 0 14 * * ?") // 매일 오후 2시에 실행
    public void runScheduledHotelDetailInfoCollectTask() {
        // 1. 호텔 리스트 조회 및 중복 제거
        List<Document> hotelList = mongoTemplate.getCollection("hotel")
                .find()
                .projection(Projections.fields(Projections.include("hotelId", "cityCode", "name"), Projections.excludeId()))
                .into(new ArrayList<>())
                .stream()
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

        // 2. 체크인 날짜 설정 (오늘 기준 2일 후)
        LocalDate today = LocalDate.now();
        LocalDate checkInDate = today.plusDays(2);
        LocalDate checkOutDate = checkInDate.plusDays(1);

        // 3. 호텔 상세 정보 생성 및 저장
        for (Document hotel : hotelList) {
            String hotelId = hotel.getString("hotelId");
            String cityCode = hotel.getString("cityCode");
            String name = hotel.getString("name");

            // 예약 코드 및 임의 값 생성
            String chainCode = generateRandomChainCode(); // 영문자 2개 즉석에서 생성
            String currency = getCurrencyByCityCode(cityCode); // cityCode에 따른 통화 설정

            // 3개의 offer 생성
            List<Document> offers = Arrays.asList(
                    createOffer(checkInDate, checkOutDate, currency),
                    createOffer(checkInDate, checkOutDate, currency),
                    createOffer(checkInDate, checkOutDate, currency)
            );

            // 4. 호텔 정보가 이미 존재하는지 확인
            Document existingHotel = mongoTemplate.getCollection("hotel_detail_info")
                    .find(new Document("hotel.hotelId", hotelId))
                    .first();

            if (existingHotel != null) {
                // 5. 호텔 정보가 존재할 경우 offers 필드만 업데이트
                mongoTemplate.getCollection("hotel_detail_info")
                        .updateOne(
                                new Document("hotel.hotelId", hotelId),
                                new Document("$set", new Document("offers", offers))
                        );
            } else {
                // 6. 호텔 정보가 존재하지 않을 경우 전체 문서 삽입
                Document hotelDetail = new Document()
                                .append("type", "hotel-offers")
                                .append("hotel", new Document()
                                        .append("type", "hotel")
                                        .append("hotelId", hotelId)
                                        .append("chainCode", chainCode)
                                        .append("dupeId", generateRandomDupeId())  // dupeId 9자리 숫자 생성
                                        .append("name", name)
                                        .append("cityCode", cityCode))
                                .append("available", true)
                                .append("offers", offers);

                mongoTemplate.getCollection("hotel_detail_info").insertOne(hotelDetail);
            }
        }
    }

    // offer 생성 메서드
    private Document createOffer(LocalDate checkInDate, LocalDate checkOutDate, String currency) {
        String offerId = generateRandomOfferId();
        double basePrice = generateRandomPrice(100, 1000);
        double totalPrice = basePrice + generateRandomPrice(10, 50); // base보다 커야 함

        // 소수점 두 자리까지 표현
        basePrice = Math.round(basePrice * 100.0) / 100.0;
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;

        int randomGuests = generateRandomGuests();


        return new Document()
                .append("offerId", offerId)
                .append("checkInDate", checkInDate.toString())
                .append("checkOutDate", checkOutDate.toString())
                .append("rateCode", "RAC")
                .append("available", true)
                .append("guests", new Document().append("adults", randomGuests))
                .append("price", new Document()
                        .append("currency", currency)
                        .append("base", basePrice)
                        .append("total", totalPrice)
                        .append("variations", new Document()
                                .append("average", new Document().append("base", basePrice))
                                .append("changes", Arrays.asList(new Document()
                                        .append("startDate", checkInDate.toString())
                                        .append("endDate", checkInDate.toString())
                                        .append("base", basePrice)))))
                .append("policies", new Document()
                        .append("cancellations", Arrays.asList(new Document()
                                .append("description", new Document().append("text", "NON-REFUNDABLE RATE"))
                                .append("type", "FULL_STAY")))
                        .append("paymentType", "deposit"));
    }

    // 임의의 예약 코드 생성 메서드
    private String generateRandomOfferId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // 임의의 체인 코드 생성 메서드 (영문자 2개 즉석 생성)
    private String generateRandomChainCode() {
        Random random = new Random();
        char firstLetter = (char) ('A' + random.nextInt(26));  // A-Z 중 임의 문자 생성
        char secondLetter = (char) ('A' + random.nextInt(26)); // A-Z 중 임의 문자 생성
        return "" + firstLetter + secondLetter;  // 두 글자로 된 체인코드 반환
    }

    // 임의의 가격 생성 메서드 (100~1000 사이)
    private double generateRandomPrice(int min, int max) {
        return min + (Math.random() * (max - min));
    }

    // 9자리 숫자 생성 메서드 (dupeId)
    private String generateRandomDupeId() {
        int dupeId = (int)(Math.random() * 900000000) + 100000000;  // 100000000 ~ 999999999 사이 숫자
        return String.valueOf(dupeId);
    }

    // cityCode에 따른 통화 반환 메서드
    private String getCurrencyByCityCode(String cityCode) {
        switch (cityCode) {
            case "LON": return "GBP";
            case "NYC": return "USD";
            case "PEK": return "CNY";
            case "TYK": return "JPY";
            case "PAR": return "EUR";
            case "SEL": return "KRW";
            default: return "USD";  // 기본 통화
        }
    }

    // 1, 2, 3 중 임의의 guests 생성 메서드
    private int generateRandomGuests() {
        Random random = new Random();
        return random.nextInt(3) + 1; // 1, 2, 3 중 임의 숫자 반환
    }


    private String buildFlightOfferUrl(String origin, String destination, String departureDate, int adults, boolean nonStop, int max) {

        return String.format(
                "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=%d&nonStop=%b&max=%d",
                origin, destination, departureDate, adults, nonStop, max
        );
    }
//
    private String buildHotelListUrl(String cityCode) {
        return String.format(
                "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=%s&radius=10&radiusUnit=KM&hotelSource=ALL",
               cityCode
        );
    }
//
//    private String buildHotelDetailInfoUrl(String hotelId, String checkInDate) {
//        return String.format(
//                "https://test.api.amadeus.com/v3/shopping/hotel-offers?hotelIds=%s&checkInDate=%s",
//                hotelId, checkInDate
//        );
//    }

    @Scheduled(cron = "0 0 15 * * ?")
    public void runScheduledDomesticAccomCollectTask() {
        domesticAccomService.fetchAndSaveDomesticAccomItems();
    }
}

// "https://test.api.amadeus.com/v3/shopping/hotel-offers?hotelId=%adults=%s&checkInDate=%s&roomQuantity=%d&paymentPolicy=NONE&bestRateOnly=true",

//private String buildHotelDetailInfoUrl(String hotelId, int adults, String checkInDate, int roomQuantity) {
//    return String.format(
//            "https://test.api.amadeus.com/v3/shopping/hotel-offers?hotelId=%d",
//            hotelId
//    );
//}