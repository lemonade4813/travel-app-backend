package com.example.travelappbackend.service;

import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addUser(UserDTO userDTO){
         userRepository.save(userDTO.toEntity());
    }

    public boolean existsByMemberId(String userId){

        return userRepository.existsByUserId(userId);
    }

}
