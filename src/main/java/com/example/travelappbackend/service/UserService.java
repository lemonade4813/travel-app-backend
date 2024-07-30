package com.example.travelappbackend.service;

import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addUser(UserDTO userDTO){

            if (userRepository.existsByUserId(userDTO.getUserId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 아이디입니다.");
            }
            userRepository.save(userDTO.toEntity());
        }

//    public boolean existsByMemberId(String userId){
//
//        return userRepository.existsByUserId(userId);
//    }

}
