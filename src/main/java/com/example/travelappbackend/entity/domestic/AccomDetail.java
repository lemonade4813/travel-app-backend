package com.example.travelappbackend.entity.domestic;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "domestic_accom_detail_info")
@Data

public class AccomDetail {

    private String id;
    private String addr1;
    private String addr2;
    private String areacode;
    private String benikia;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentid;
    private String contenttypeid;
    private String createdtime;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private String goodstay;
    private String hanok;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String modifiedtime;
    private String tel;
    private String title;
    private String booktour;
    private String sigungucode;

    private List<AccomAvailInfo> availInfo;
}
