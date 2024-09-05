package com.example.travelappbackend.repository.domestic;

import com.example.travelappbackend.entity.domestic.PurchaseAccomItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseAccomItemRepository extends MongoRepository<PurchaseAccomItem, String> {

}
