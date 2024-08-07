package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.repository.domestic.AccomRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class DomesticAccomService {

    private final RestTemplate restTemplate;
    private final AccomRepository accomRepository;

    public DomesticAccomService(RestTemplate restTemplate, AccomRepository accomRepository){
        this.restTemplate = restTemplate;
        this.accomRepository = accomRepository;
    }

    public List<Accom> getDomesticAccom(){
        return accomRepository.findAll();
    }

    public void fetchAndSaveDomesticAccomItems() {

        String url ="https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey=GagNlrULGxksg16%2B71Pvi19nM5wOAy66KUlK5LF%2FfIXAe7fOeEPl3FyOBEJbnil91it6z5BSFNXDMxUMI9qEZg%3D%3D&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                List<Accom> accomsItems = Arrays.asList(objectMapper.convertValue(itemsNode, Accom[].class));
                accomRepository.saveAll(accomsItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
