package com.example.travelappbackend.repository.user;

import com.example.travelappbackend.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);

}



