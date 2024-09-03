package com.example.travelappbackend.repository.domestic;


import com.example.travelappbackend.entity.domestic.AccomDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccomDetailRepository extends MongoRepository<AccomDetail, String> {
    AccomDetail findByContentid(String contentid);

}
