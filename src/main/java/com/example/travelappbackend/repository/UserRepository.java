package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    boolean existsByUserId(String userId);
}



