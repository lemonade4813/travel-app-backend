//package com.example.travelappbackend.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class AmadeusFlightService {
//
//    private final RestTemplate restTemplate;
//    private final AmadeusApiKeyService amadeusApiKeyService;
//
//    @Autowired
//    public AmadeusFlightService(RestTemplate restTemplate, AmadeusApiKeyService amadeusApiKeyService) {
//        this.restTemplate = restTemplate;
//        this.amadeusApiKeyService = amadeusApiKeyService;
//    }
//
//    public Object fetchData(String url) {
//        try {
//            String token = getAccessToken();
//            if (token == null) throw new RuntimeException("Failed to get access token");
//
//            return makeApiCall(url, token);
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                String newToken = amadeusApiKeyService.getAmadeusAccessToken();
//                if (newToken == null) throw new RuntimeException("Failed to refresh access token");
//
//                return makeApiCall(url, newToken);
//            } else {
//                throw e;
//            }
//        }
//    }
//
//    private Object makeApiCall(String url, String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setOrigin("http://192.168.45.127");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
//
//        return response.getBody();
//    }
//
//    private String getAccessToken() {
//        return amadeusApiKeyService.getAmadeusAccessToken();
//    }
//
//}

package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.FlightData;
import com.example.travelappbackend.repository.FlightDataRepository;
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
    private final FlightDataRepository flightDataRepository;

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate, AmadeusApiKeyService amadeusApiKeyService, FlightDataRepository flightDataRepository) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.flightDataRepository = flightDataRepository;
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
            for (JsonNode flight : data) {
                FlightData flightData = new FlightData();
                flightData.setType(flight.path("type").asText());
                flightData.setOfferId(flight.path("id").asInt());
                flightData.setCurrency(flight.path("price").path("currency").asText());
                flightData.setTotal(flight.path("price").path("total").asText());
                flightData.setBase(flight.path("price").path("base").asText());
                flightData.setOneWay(flight.path("oneWay").asBoolean());
                flightData.setLastTicketingDate(flight.path("lastTicketingDate").asText());
                flightData.setOriginLocationCode(originLocationCode);
                flightData.setDestinationLocationCode(destinationLocationCode);

                flightDataRepository.save(flightData);
            }
        }
    }

    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }
}