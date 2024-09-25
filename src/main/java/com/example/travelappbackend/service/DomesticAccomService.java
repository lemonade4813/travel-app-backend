package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.domestic.Accom;
import com.example.travelappbackend.entity.domestic.AccomAvailInfo;
import com.example.travelappbackend.entity.domestic.AccomDetail;
import com.example.travelappbackend.entity.domestic.PurchaseAccomItem;
import com.example.travelappbackend.model.PurchaseAccomItemDTO;
import com.example.travelappbackend.repository.domestic.AccomDetailRepository;
import com.example.travelappbackend.repository.domestic.AccomRepository;
import com.example.travelappbackend.repository.domestic.PurchaseAccomItemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class DomesticAccomService {

    private final RestTemplate restTemplate;
    private final AccomRepository accomRepository;

    private final AccomDetailRepository accomDetailRepository;

    private final PurchaseAccomItemRepository purchaseAccomItemRepository;

    public DomesticAccomService(RestTemplate restTemplate, AccomRepository accomRepository, AccomDetailRepository accomDetailRepository, PurchaseAccomItemRepository purchaseAccomItemRepository) {
        this.restTemplate = restTemplate;
        this.accomRepository = accomRepository;
        this.accomDetailRepository = accomDetailRepository;
        this.purchaseAccomItemRepository = purchaseAccomItemRepository;

    }

    public List<Accom> getDomesticAccom() {
        return accomRepository.findAll();
    }

    public AccomDetail getDomesticAccomDetailInfo(String contentId) {
        return accomDetailRepository.findByContentid(contentId);
    }

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

                // Save or update AccomDetail items
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
        aTypeInfo.setPrice((random.nextInt((max - min) / 1000 + 1) * 1000) + min);
        aTypeInfo.setAvailCount(random.nextInt(10) + 1);
        aTypeInfo.setCheckInDate(formattedDate);
        aTypeInfo.setType("Type A");

        AccomAvailInfo bTypeInfo = new AccomAvailInfo();
        bTypeInfo.setPrice((random.nextInt((max - min) / 1000 + 1) * 1000) + min);
        bTypeInfo.setAvailCount(random.nextInt(10) + 1);
        bTypeInfo.setCheckInDate(formattedDate);
        bTypeInfo.setType("Type B");

        availInfoList.add(aTypeInfo);
        availInfoList.add(bTypeInfo);

        return availInfoList;
    }


    public boolean updateAvailCount(String contentid, String itemId, int number) {
        AccomDetail existingAccomDetail = accomDetailRepository.findByContentid(contentid);


        if (existingAccomDetail != null && existingAccomDetail.getAvailInfo() != null) {
            Optional<AccomAvailInfo> availInfoOpt = existingAccomDetail.getAvailInfo().stream()
                    .filter(info -> info.getItemId().equals(itemId))
                    .findFirst();

            System.out.println(availInfoOpt);

            if (availInfoOpt.isPresent()) {
                AccomAvailInfo availInfo = availInfoOpt.get();

                if (availInfo.getAvailCount() > 0) {
                    availInfo.setAvailCount(availInfo.getAvailCount() + number);
                    accomDetailRepository.save(existingAccomDetail);
                    return true;
                } else {
                    throw new IllegalStateException("예약 가능 수를 초과하였습니다.");
                }
            }
        }
        throw new IllegalArgumentException("예약 정보 조회에 실패하였습니다.");
    }


    public void createAccomPurchase(String contentid, String itemId, String userId, int price, String type, String title) {
        PurchaseAccomItem accomPurchaseItem = new PurchaseAccomItem();

        accomPurchaseItem.setContentid(contentid);
        accomPurchaseItem.setItemId(itemId);
        accomPurchaseItem.setUserId(userId);
        accomPurchaseItem.setPrice(price);
        accomPurchaseItem.setTitle(title);
        accomPurchaseItem.setType(type);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        accomPurchaseItem.setPurchaseDate(formattedDate);

        purchaseAccomItemRepository.save(accomPurchaseItem);

    }

    public void conductAccomPurchase(PurchaseAccomItemDTO purchaseAccomItemDTO) {
        String contentid = purchaseAccomItemDTO.getContentid();
        String itemId = purchaseAccomItemDTO.getItemId();
        int price = purchaseAccomItemDTO.getPrice();
        String type = purchaseAccomItemDTO.getType();
        String title = purchaseAccomItemDTO.getTitle();
        String userId = purchaseAccomItemDTO.getUserId();

        try {

            System.out.println("userId !!!!!!" + userId);
            boolean updated = updateAvailCount(contentid, itemId, -1);

            if (updated) {
                createAccomPurchase(contentid, itemId, userId, price, type, title);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid accommodation detail or item ID.");
        } catch (IllegalStateException e) {
            throw new RuntimeException("Insufficient availability for item purchase.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred during the purchase process.");
        }
    }
    public List<PurchaseAccomItem> getAccomPurchaseInfo (String userId){

       return purchaseAccomItemRepository.findPurchaseAccomItemsBy(userId);

    }

    public String deleteAccomPurchase (String contentid, String itemId, String purchaseId) {
        try {
            boolean exists = purchaseAccomItemRepository.existsByPurchaseId(purchaseId);
                if(exists) {
                    purchaseAccomItemRepository.deleteByPurchaseId(purchaseId);
                    updateAvailCount(contentid, itemId, 1);

                }
            } catch (Exception e){
                throw new RuntimeException("예약 삭제에 실패하였습니다.");
        }
        return "정상적으로 삭제가 완료되었습니다.";
    }
}
