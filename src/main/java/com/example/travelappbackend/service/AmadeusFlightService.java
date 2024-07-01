package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.FlightInfo;
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

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate, AmadeusApiKeyService amadeusApiKeyService, FlightInfoRepository flightInfoRepository) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.flightInfoRepository = flightInfoRepository;
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

//        System.out.println("response : " + response);

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
                FlightInfo flightInfo = new FlightInfo();
                flightInfo.setType(flightData.path("type").asText());
                flightInfo.setOfferId(flightData.path("id").asInt());
                flightInfo.setCurrency(flightData.path("price").path("currency").asText());
                flightInfo.setTotal(flightData.path("price").path("total").asText());
                flightInfo.setBase(flightData.path("price").path("base").asText());
                flightInfo.setOneWay(flightData.path("oneWay").asBoolean());
                flightInfo.setLastTicketingDate(flightData.path("lastTicketingDate").asText());
                flightInfo.setOriginLocationCode(originLocationCode);
                flightInfo.setDestinationLocationCode(destinationLocationCode);

                flightInfoRepository.save(flightInfo);
            }
        }
    }

    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }
}