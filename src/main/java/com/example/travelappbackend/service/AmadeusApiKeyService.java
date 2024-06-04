package com.example.travelappbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AmadeusApiKeyService {

    @Value("${amadeus.api.key}")
    private String amadeusApiKey;

    @Value("${amadeus.secret.key}")
    private String amadeusSecretKey;

    private final RestTemplate restTemplate;

    public AmadeusApiKeyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAmadeusAccessToken() {
        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials&client_id=" + amadeusApiKey + "&client_secret=" + amadeusSecretKey;

        System.out.println(body);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        System.out.println(request);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            System.out.println(response);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);

                return root.path("access_token").asText();
            } else {
                System.err.println("Failed to get access token, status code: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while getting access token: " + e.getMessage());
            return null;
        }
    }
}