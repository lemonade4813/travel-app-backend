package com.example.travelappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AmadeusFlightService {

    private final RestTemplate restTemplate;
    private final AmadeusApiKeyService amadeusService;

    @Autowired
    public AmadeusFlightService(RestTemplate restTemplate, AmadeusApiKeyService amadeusService) {
        this.restTemplate = restTemplate;
        this.amadeusService = amadeusService;
    }

    public Object fetchData(String url) {
        try {
            String token = getAccessToken();
            if (token == null) throw new RuntimeException("Failed to get access token");

            return makeApiCall(url, token);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newToken = amadeusService.getAmadeusAccessToken();
                if (newToken == null) throw new RuntimeException("Failed to refresh access token");

                return makeApiCall(url, newToken);
            } else {
                throw e;
            }
        }
    }

    private Object makeApiCall(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setOrigin("http://192.168.45.127");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        return response.getBody();
    }

    private String getAccessToken() {
        return amadeusService.getAmadeusAccessToken();
    }
}