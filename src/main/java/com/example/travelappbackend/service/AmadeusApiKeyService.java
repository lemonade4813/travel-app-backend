package com.example.travelappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Configuration
public class AmadeusApiKeyService {

    @Value("${amadeus.api.key}")
    private String amadeusApiKey;

    @Value("${amadeus.secret.key}")
    private String amadeusSecretKey;

    private final RestTemplate restTemplate;

    @Autowired
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

    public JsonNode makeApiCall(String url, String token) {
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
}