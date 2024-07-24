package com.example.travelappbackend.repository.user;

import com.example.travelappbackend.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    boolean existsByUserId(String userId);
    User findByUserId(String userId);

}



