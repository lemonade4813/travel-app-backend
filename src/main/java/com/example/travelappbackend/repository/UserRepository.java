package com.example.travelappbackend.repository;

import com.example.travelappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserId(String userId);

}
