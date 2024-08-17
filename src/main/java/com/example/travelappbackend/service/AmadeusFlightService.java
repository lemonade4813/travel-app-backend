package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.logs.FlightCollectionLogs;
import com.example.travelappbackend.entity.flight.FlightDetailInfo;
import com.example.travelappbackend.entity.flight.FlightInfo;
import com.example.travelappbackend.entity.logs.HotelCollectionLogs;
import com.example.travelappbackend.repository.flight.FlightCollectionLogsRepository;
import com.example.travelappbackend.repository.flight.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.flight.FlightInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AmadeusFlightService {

    private final RestTemplate restTemplate;
    private final AmadeusApiKeyService amadeusApiKeyService;
    private final FlightInfoRepository flightInfoRepository;
    private final FlightDetailInfoRepository flightDetailInfoRepository;
    private final FlightCollectionLogsRepository flightCollectionLogsRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate,
                                AmadeusApiKeyService amadeusApiKeyService,
                                FlightInfoRepository flightInfoRepository,
                                FlightDetailInfoRepository flightDetailInfoRepository,
                                FlightCollectionLogsRepository flightCollectionLogsRepository,
                                MongoTemplate mongoTemplate
                                ) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.flightInfoRepository = flightInfoRepository;
        this.flightDetailInfoRepository = flightDetailInfoRepository;
        this.flightCollectionLogsRepository = flightCollectionLogsRepository;
        this.mongoTemplate = mongoTemplate;

    }

    public void fetchAvailFlightData(String url) {

        try {

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = formatter.format(today);

            Optional<HotelCollectionLogs> log = flightCollectionLogsRepository.findByCollectionDateAndSaveSuccess(date, true);

            // 로그가 존재하고 saveSuccess가 true인 경우 메서드를 종료합니다.
            if (log.isPresent()) {
                System.out.println("금일 수집한 항공편 데이터 리스트가 이미 존재합니다.");
                return;
            }
            
            String token = getAccessToken();
            if (token == null) throw new RuntimeException("Failed to get access token");

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            String originLocationCode = builder.build().getQueryParams().getFirst("originLocationCode");
            String destinationLocationCode = builder.build().getQueryParams().getFirst("destinationLocationCode");

            JsonNode response = amadeusApiKeyService.makeApiCall(url, token);
            parseAndSaveFlightAvailData(response, originLocationCode, destinationLocationCode);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newToken = amadeusApiKeyService.getAmadeusAccessToken();
                if (newToken == null) throw new RuntimeException("Failed to refresh access token");

                JsonNode response = amadeusApiKeyService.makeApiCall(url, newToken);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                String originLocationCode = builder.build().getQueryParams().getFirst("originLocationCode");
                String destinationLocationCode = builder.build().getQueryParams().getFirst("destinationLocationCode");
                parseAndSaveFlightAvailData(response, originLocationCode, destinationLocationCode);
            } else {
                throw e;
            }
        }
    }

    @Transactional
    public void saveFlightInfoAndDetail(FlightInfo flightInfo, FlightDetailInfo flightDetailInfo) {
        flightInfoRepository.save(flightInfo);
        flightDetailInfoRepository.save(flightDetailInfo);
    }


    private void parseAndSaveFlightAvailData(JsonNode response,
                                  String originLocationCode,
                                  String destinationLocationCode) {
        JsonNode data = response.path("data");
        boolean success = true;


        try {
            if (data.isArray()) {
                for (JsonNode flightData : data) {

                    String type = flightData.path("type").asText();
                    int offerId = flightData.path("id").asInt();
                    String currency = flightData.path("price").path("currency").asText();
                    String total = flightData.path("price").path("total").asText();
                    String base = flightData.path("price").path("base").asText();
                    boolean oneway = flightData.path("oneWay").asBoolean();
                    String lastTicketingDate = flightData.path("lastTicketingDate").asText();
                    int numberOfBookableSeats = flightData.path("numberOfBookableSeats").asInt();

                    // 항공편 예약 제공 정보 테이블에 데이터 저장

                    FlightInfo flightInfo = new FlightInfo();
                    flightInfo.setType(type);
                    flightInfo.setOfferId(offerId);
                    flightInfo.setCurrency(currency);
                    flightInfo.setTotal(total);
                    flightInfo.setBase(base);
                    flightInfo.setOneWay(oneway);
                    flightInfo.setLastTicketingDate(lastTicketingDate);
                    flightInfo.setOriginLocationCode(originLocationCode);
                    flightInfo.setDestinationLocationCode(destinationLocationCode);
                    flightInfo.setNumberOfBookableSeats(numberOfBookableSeats);

                    // 항공편 예약 제공 상세 정보 테이블에 데이터 저장

                    Document doc = Document.parse(flightData.toString());
                    doc.put("offerId", offerId);
                    doc.remove("id");
                    mongoTemplate.insert(doc, "flight_detail_info");
                }
            }

        } catch (Exception e) {
            success  = false;
            throw e;
        } finally {
            FlightCollectionLogs log = new FlightCollectionLogs();

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            log.setCollectionDate(today.format(formatter));
            log.setSaveSuccess(success);
            flightCollectionLogsRepository.save(log);
        }
    }


    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }


}