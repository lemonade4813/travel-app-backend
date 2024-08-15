package com.example.travelappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public <T> List<T> getDataByQueryParams(Map<String, Object> queryParams, Class<T> entityClass){

    Query query = new Query();

    queryParams.forEach((key, value)->{
        if(value != null){
            query.addCriteria(Criteria.where(key).is(value));
        }
    });

    return mongoTemplate.find(query, entityClass);
    }


}
