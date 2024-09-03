package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.entity.domestic.AccomAvailInfo;
import com.example.travelappbackend.entity.domestic.AccomDetail;
import com.example.travelappbackend.repository.domestic.AccomDetailRepository;
import com.example.travelappbackend.repository.domestic.AccomRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Service
public class DomesticAccomService {

    private final RestTemplate restTemplate;
    private final AccomRepository accomRepository;

    private final AccomDetailRepository accomDetailRepository;

    public DomesticAccomService(RestTemplate restTemplate, AccomRepository accomRepository, AccomDetailRepository accomDetailRepository){
        this.restTemplate = restTemplate;
        this.accomRepository = accomRepository;
        this.accomDetailRepository = accomDetailRepository;

    }

    public List<Accom> getDomesticAccom(){
        return accomRepository.findAll();
    }

    public Accom getDomesticAccomDetailInfo(String contentId){return  accomRepository.findByContentid(contentId);}

//    public void fetchAndSaveDomesticAccomItems() {
//        try {
//            String url = "https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey=GagNlrULGxksg16%2B71Pvi19nM5wOAy66KUlK5LF%2FfIXAe7fOeEPl3FyOBEJbnil91it6z5BSFNXDMxUMI9qEZg%3D%3D&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A";
//
//            URI uri = new URI(url);
//
//            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(response.getBody());
//            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
//
//            if (itemsNode.isArray()) {
//                List<Accom> accomItems = Arrays.asList(objectMapper.convertValue(itemsNode, Accom[].class));
//                accomRepository.saveAll(accomItems);
//
//
//                List<AccomDetail> accomDetails = Arrays.asList(objectMapper.convertValue(itemsNode, AccomDetail[].class));
//
//                Random random = new Random();
//
//                int min = 100_000;
//                int max = 300_000;
//
//
//                for (AccomDetail accomDetail : accomDetails) {
//                    int aTypePrice = (random.nextInt((max - min) / 1000 + 1) * 1000) + min;
//                    int bTypePrice = (random.nextInt((max - min) / 1000 + 1) * 1000) + min;
//
//                    int aTypeAvailCount = random.nextInt(10) + 1;
//                    int bTypeAvailCount = random.nextInt(10) + 1;
//
//                    // 생성된 랜덤 값을 accomDetail 객체에 설정
//                    accomDetail.setATypePrice(aTypePrice);
//                    accomDetail.setBTypePrice(bTypePrice);
//                    accomDetail.setATypeAvailCount(aTypeAvailCount);
//                    accomDetail.setBTypeAvailCount(bTypeAvailCount);
//
//                    LocalDate today = LocalDate.now();
//
//                    // 현재 날짜에서 7일 후의 날짜 계산
//                    LocalDate sevenDaysLater = today.plusDays(7);
//
//                    // 날짜를 yyyy-MM-dd 형식으로 포맷팅
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    String formattedDate = sevenDaysLater.format(formatter);
//
//                    accomDetail.setCheckInDate(formattedDate);
//                }
//
//                accomDetailRepository.saveAll(accomDetails);
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void fetchAndSaveDomesticAccomItems() {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey=GagNlrULGxksg16%2B71Pvi19nM5wOAy66KUlK5LF%2FfIXAe7fOeEPl3FyOBEJbnil91it6z5BSFNXDMxUMI9qEZg%3D%3D&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A";
            URI uri = new URI(url);
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                List<Accom> accomItems = Arrays.asList(objectMapper.convertValue(itemsNode, Accom[].class));
                List<AccomDetail> accomDetails = Arrays.asList(objectMapper.convertValue(itemsNode, AccomDetail[].class));

                Random random = new Random();
                int min = 100_000;
                int max = 300_000;

                for (Accom accom : accomItems) {
                    Accom existingAccom = accomRepository.findByContentid(accom.getContentid());
                    if (existingAccom != null) {
                        accom.setId(existingAccom.getId()); // ID 설정하여 덮어쓰기
                    }
                    accomRepository.save(accom);
                }

                for (AccomDetail accomDetail : accomDetails) {
                    int aTypePrice = (random.nextInt((max - min) / 1000 + 1) * 1000) + min;
                    int bTypePrice = (random.nextInt((max - min) / 1000 + 1) * 1000) + min;

                    int aTypeAvailCount = random.nextInt(10) + 1;
                    int bTypeAvailCount = random.nextInt(10) + 1;

                    AccomAvailInfo accomAvailInfo = new AccomAvailInfo();

                    accomAvailInfo.setATypePrice(aTypePrice);
                    accomAvailInfo.setBTypePrice(bTypePrice);
                    accomAvailInfo.setATypeAvailCount(aTypeAvailCount);
                    accomAvailInfo.setBTypeAvailCount(bTypeAvailCount);

                    LocalDate today = LocalDate.now();
                    LocalDate sevenDaysLater = today.plusDays(7);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = sevenDaysLater.format(formatter);

                    accomAvailInfo.setCheckInDate(formattedDate);

                    AccomDetail existingAccomDetail = accomDetailRepository.findByContentid(accomDetail.getContentid());
                    if (existingAccomDetail != null) {
                        accomDetail.setId(existingAccomDetail.getId()); // ID 설정하여 덮어쓰기
                    }
                    accomDetailRepository.save(accomDetail);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
