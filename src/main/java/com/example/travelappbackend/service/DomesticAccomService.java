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
import java.util.ArrayList;
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

    public void fetchAndSaveDomesticAccomItems() {
        try {
            String url = "https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey=GagNlrULGxksg16%2B71Pvi19nM5wOAy66KUlK5LF%2FfIXAe7fOeEPl3FyOBEJbnil91it6z5BSFNXDMxUMI9qEZg%3D%3D&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A";
            URI uri = new URI(url);
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                List<Accom> accomItems = objectMapper.convertValue(itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, Accom.class));
                List<AccomDetail> accomDetails = objectMapper.convertValue(itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, AccomDetail.class));


                for (Accom accom : accomItems) {
                    saveAccomIfNotExists(accom);
                }

                for (AccomDetail accomDetail : accomDetails) {
                    updateOrSaveAccomDetail(accomDetail);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAccomIfNotExists(Accom accom) {
        Accom existingAccom = accomRepository.findByContentid(accom.getContentid());
        if (existingAccom == null) {
            accomRepository.save(accom);
        }
    }

    private void updateOrSaveAccomDetail(AccomDetail accomDetail) {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = sevenDaysLater.format(formatter);

        AccomDetail existingAccomDetail = accomDetailRepository.findByContentid(accomDetail.getContentid());

        if (existingAccomDetail != null) {

            List<AccomAvailInfo> existingAvailInfoList = existingAccomDetail.getAvailInfo();
            boolean existsForDate = existingAvailInfoList.stream()
                    .anyMatch(availInfo -> formattedDate.equals(availInfo.getCheckInDate()));

            if (!existsForDate) {

                List<AccomAvailInfo> newAvailInfoList = createAvailInfoList(formattedDate);
                existingAvailInfoList.addAll(newAvailInfoList);
                existingAccomDetail.setAvailInfo(existingAvailInfoList);


                accomDetailRepository.save(existingAccomDetail);
            }
        } else {

            List<AccomAvailInfo> availInfoList = createAvailInfoList(formattedDate);
            accomDetail.setAvailInfo(availInfoList);
            accomDetailRepository.save(accomDetail);
        }
    }

    private List<AccomAvailInfo> createAvailInfoList(String formattedDate) {
        Random random = new Random();
        int min = 100_000;
        int max = 300_000;

        List<AccomAvailInfo> availInfoList = new ArrayList<>();


        AccomAvailInfo aTypeInfo = new AccomAvailInfo();
        aTypeInfo.setATypePrice((random.nextInt((max - min) / 1000 + 1) * 1000) + min);
        aTypeInfo.setATypeAvailCount(random.nextInt(10) + 1);
        aTypeInfo.setCheckInDate(formattedDate);


        AccomAvailInfo bTypeInfo = new AccomAvailInfo();
        bTypeInfo.setBTypePrice((random.nextInt((max - min) / 1000 + 1) * 1000) + min);
        bTypeInfo.setBTypeAvailCount(random.nextInt(10) + 1);
        bTypeInfo.setCheckInDate(formattedDate);

        availInfoList.add(aTypeInfo);
        availInfoList.add(bTypeInfo);

        return availInfoList;
    }
}
