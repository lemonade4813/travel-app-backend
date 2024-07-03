package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.FlightDetailInfo;
import com.example.travelappbackend.entity.FlightInfo;
import com.example.travelappbackend.repository.FlightDetailInfoRepository;
import com.example.travelappbackend.repository.FlightInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AmadeusFlightService {

    private final RestTemplate restTemplate;
    private final AmadeusApiKeyService amadeusApiKeyService;
    private final FlightInfoRepository flightInfoRepository;
    private final FlightDetailInfoRepository flightDetailInfoRepository;

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate,
                                AmadeusApiKeyService amadeusApiKeyService,
                                FlightInfoRepository flightInfoRepository,
                                FlightDetailInfoRepository flightDetailInfoRepository) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.flightInfoRepository = flightInfoRepository;
        this.flightDetailInfoRepository = flightDetailInfoRepository;
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

    private void parseAndSaveData(JsonNode response, String originLocationCode, String destinationLocationCode) {
        JsonNode data = response.path("data");

        System.out.println("datalist : " + data);
        if (data.isArray()) {
            for (JsonNode flightData : data) {

                String type = flightData.path("type").asText();
                int offerId = flightData.path("id").asInt();
                String currency = flightData.path("price").path("currency").asText();
                String total = flightData.path("price").path("total").asText();
                String base = flightData.path("price").path("base").asText();
                boolean oneway = flightData.path("oneWay").asBoolean();
                String lastTicketingDate = flightData.path("lastTicketingDate").asText();

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

                flightInfoRepository.save(flightInfo);


                // 항공편 예약 제공 상세 정보 테이블에 데이터 저장

                FlightDetailInfo flightDetailInfo = new FlightDetailInfo();

                flightDetailInfo.setType(type);
                flightDetailInfo.setOfferId(offerId);
                flightDetailInfo.setCurrency(currency);
                flightDetailInfo.setTotal(total);
                flightDetailInfo.setBase(base);
                flightDetailInfo.setOneWay(oneway);
                flightDetailInfo.setLastTicketingDate(lastTicketingDate);
                flightDetailInfo.setOriginLocationCode(originLocationCode);
                flightDetailInfo.setDestinationLocationCode(destinationLocationCode);

                flightDetailInfoRepository.save(flightDetailInfo);

            }
        }
    }

    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }
}