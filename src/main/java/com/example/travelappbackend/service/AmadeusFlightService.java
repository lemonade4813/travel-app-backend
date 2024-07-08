package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.FlightCollectionLogs;
import com.example.travelappbackend.entity.FlightDetailInfo;
import com.example.travelappbackend.entity.FlightInfo;
import com.example.travelappbackend.entity.Segment;
import com.example.travelappbackend.repository.FlightCollectionLogsRepository;
import com.example.travelappbackend.repository.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.FlightInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmadeusFlightService {

    private final RestTemplate restTemplate;
    private final AmadeusApiKeyService amadeusApiKeyService;
    private final FlightInfoRepository flightInfoRepository;
    private final FlightDetailInfoRepository flightDetailInfoRepository;
    private final FlightCollectionLogsRepository flightCollectionLogsRepository;

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate,
                                AmadeusApiKeyService amadeusApiKeyService,
                                FlightInfoRepository flightInfoRepository,
                                FlightDetailInfoRepository flightDetailInfoRepository,
                                FlightCollectionLogsRepository flightCollectionLogsRepository) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.flightInfoRepository = flightInfoRepository;
        this.flightDetailInfoRepository = flightDetailInfoRepository;
        this.flightCollectionLogsRepository = flightCollectionLogsRepository;
    }

    public void fetchData(String url) {
        try {
            String token = getAccessToken();
            if (token == null) throw new RuntimeException("Failed to get access token");

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            String originLocationCode = builder.build().getQueryParams().getFirst("originLocationCode");
            String destinationLocationCode = builder.build().getQueryParams().getFirst("destinationLocationCode");

            JsonNode response = makeApiCall(url, token);
            parseAndSaveData(response, originLocationCode, destinationLocationCode);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newToken = amadeusApiKeyService.getAmadeusAccessToken();
                if (newToken == null) throw new RuntimeException("Failed to refresh access token");

                JsonNode response = makeApiCall(url, newToken);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                String originLocationCode = builder.build().getQueryParams().getFirst("originLocationCode");
                String destinationLocationCode = builder.build().getQueryParams().getFirst("destinationLocationCode");
                parseAndSaveData(response, originLocationCode, destinationLocationCode);
            } else {
                throw e;
            }
        }
    }

    private JsonNode makeApiCall(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setOrigin("http://192.168.45.127");

        System.out.println("url" + url);
        System.out.println("token" + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    @Transactional
    public void saveFlightInfoAndDetail(FlightInfo flightInfo, FlightDetailInfo flightDetailInfo) {
        flightInfoRepository.save(flightInfo);
        flightDetailInfoRepository.save(flightDetailInfo);
    }


    private void parseAndSaveData(JsonNode response,
                                  String originLocationCode,
                                  String destinationLocationCode) {
        JsonNode data = response.path("data");
        boolean success = true;

        System.out.println("datalist : " + data);

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

                    FlightDetailInfo flightDetailInfo = new FlightDetailInfo();

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode segmentsNode = flightData.path("itineraries").path("segments");


                    List<Segment> segments = new ArrayList<>();

                    if (segmentsNode.isArray()) {
                        for (JsonNode segmentNode : segmentsNode) {
                            try {
                                Segment segment = mapper.treeToValue(segmentNode, Segment.class);
                                segments.add(segment);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    flightDetailInfo.setType(type);
                    flightDetailInfo.setOfferId(offerId);
                    flightDetailInfo.setCurrency(currency);
                    flightDetailInfo.setTotal(total);
                    flightDetailInfo.setBase(base);
                    flightDetailInfo.setOneWay(oneway);
                    flightDetailInfo.setLastTicketingDate(lastTicketingDate);
                    flightDetailInfo.setOriginLocationCode(originLocationCode);
                    flightDetailInfo.setDestinationLocationCode(destinationLocationCode);
                    flightDetailInfo.setSegments(segments);

                    saveFlightInfoAndDetail(flightInfo, flightDetailInfo);

                }
            }

        } catch (Exception e) {
            success  = false;
            throw e;
        } finally {
            FlightCollectionLogs log = new FlightCollectionLogs();
            log.setCollectionDate(new Date());
            log.setSaveSuccess(success);
            flightCollectionLogsRepository.save(log);
        }
    }


    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }
}