package com.example.travelappbackend.repository.domestic;

import com.example.travelappbackend.entity.domestic.Accom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccomRepository extends MongoRepository<Accom, String> {
}
