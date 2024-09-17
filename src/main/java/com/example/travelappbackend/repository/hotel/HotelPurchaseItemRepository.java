package com.example.travelappbackend.repository.hotel;


import com.example.travelappbackend.entity.hotel.PurchaseHotelItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelPurchaseItemRepository extends MongoRepository<PurchaseHotelItem, String> {
    boolean existsByPurchaseId(String purchaseId);

    void deleteByPurchaseId(String purchaseId);
}
