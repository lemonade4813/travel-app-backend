package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.hotel.Hotel;
import com.example.travelappbackend.entity.logs.HotelCollectionLogs;
import com.example.travelappbackend.repository.hotel.HotelCollectionLogsRepository;
import com.example.travelappbackend.repository.hotel.HotelInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AmadeusHotelService {

    private final RestTemplate restTemplate;
    private final AmadeusApiKeyService amadeusApiKeyService;
    private final HotelInfoRepository hotelInfoRepository;
    private final HotelCollectionLogsRepository hotelCollectionLogsRepository;


    @Autowired
    public AmadeusHotelService(RestTemplate restTemplate,
                               AmadeusApiKeyService amadeusApiKeyService,
                               HotelInfoRepository hotelInfoRepository,
                               HotelCollectionLogsRepository hotelCollectionLogsRepository,
                               MongoTemplate mongoTemplate
    ) {
        this.restTemplate = restTemplate;
        this.amadeusApiKeyService = amadeusApiKeyService;
        this.hotelInfoRepository = hotelInfoRepository;
        this.hotelCollectionLogsRepository = hotelCollectionLogsRepository;

    }


    public void fetchAvailHotelData(String url) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(today);

        Optional<HotelCollectionLogs> log = hotelCollectionLogsRepository.findByCollectionDateAndSaveSuccess(date, true);

        // 로그가 존재하고 saveSuccess가 true인 경우 메서드를 종료합니다.
        if (log.isPresent()) {
            System.out.println("금일 수집한 호텔 데이터 리스트가 이미 존재합니다.");
            return;
        }

        try {
            String token = getAccessToken();
            if (token == null) throw new RuntimeException("Failed to get access token");

            JsonNode response = amadeusApiKeyService.makeApiCall(url, token);
            parseAndSaveHotelAvailData(response);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newToken = amadeusApiKeyService.getAmadeusAccessToken();
                if (newToken == null) throw new RuntimeException("Failed to refresh access token");

                JsonNode response = amadeusApiKeyService.makeApiCall(url, newToken);
                parseAndSaveHotelAvailData(response);
            } else {
                throw e;
            }
        }
    }

    private void parseAndSaveHotelAvailData(JsonNode response) {

        JsonNode data = response.path("data");
        boolean success = true;

        System.out.println("datalist : " + data);

        try {
            if (data.isArray()) {
                for (JsonNode HotelListData : data) {

                    String type = HotelListData.path("name").asText();
                    String hotelId = HotelListData.path("hotelId").asText();
                    String distance = HotelListData.path("distance").path("value").asText();
                    String unit = HotelListData.path("distance").path("init").asText();

                    Double latitude = HotelListData.path("geoCode").path("latitude").asDouble();
                    Double longitude = HotelListData.path("geoCode").path("longitude").asDouble();

                    String countryCode = HotelListData.path("address").path("countryCode").asText();

//                    ObjectMapper mapper = new ObjectMapper();
//                    Geocode geocode = mapper.treeToValue(HotelListData.path("geoCode"), Geocode.class);

                    // 항공편 예약 제공 정보 테이블에 데이터 저장

                    Hotel hotel= new Hotel();
                    hotel.setName(type);
                    hotel.setHotelId(hotelId);
                    hotel.setDistance(distance);
                    hotel.setUnit(unit);

                    hotel.setLatitude(latitude);
                    hotel.setLongitude(longitude);

//                    hotel.setGeocode(geocode);

                    hotel.setCountryCode(countryCode);

                    hotelInfoRepository.save(hotel);

                }
            }

        } catch (Exception e) {
            success  = false;
            throw e;
        } finally {
            HotelCollectionLogs log = new HotelCollectionLogs();

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            log.setCollectionDate(today.format(formatter));
            log.setSaveSuccess(success);
            hotelCollectionLogsRepository.save(log);
        }
    }


    public void fetchHotelDetailData(String url) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(today);

        try {
            String token = getAccessToken();
            if (token == null) throw new RuntimeException("Failed to get access token");

            JsonNode response = amadeusApiKeyService.makeApiCall(url, token);
            parseAndSaveHotelAvailData(response);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                String newToken = amadeusApiKeyService.getAmadeusAccessToken();
                if (newToken == null) throw new RuntimeException("Failed to refresh access token");

                JsonNode response = amadeusApiKeyService.makeApiCall(url, newToken);
                parseAndSaveHotelAvailData(response);
            } else {
                throw e;
            }
        }
    }










    private String getAccessToken() {
        return amadeusApiKeyService.getAmadeusAccessToken();
    }

}
