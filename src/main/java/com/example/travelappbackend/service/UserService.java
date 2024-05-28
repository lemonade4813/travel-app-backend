package com.example.travelappbackend.service;

import com.example.travelappbackend.entity.User;
import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserDTO userDTO){
         userRepository.save(userDTO.toEntity());
    }

    public boolean existsByMemberId(String userId){

        return userRepository.existsUserId(userId);
    }

}
