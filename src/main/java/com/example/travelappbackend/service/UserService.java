package com.example.travelappbackend.service;

import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public void addUser(UserDTO userDTO){

            if (userRepository.existsByUserId(userDTO.getUserId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 아이디입니다.");
            }

            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(userDTO.toEntity());
        }

//    public boolean existsByMemberId(String userId){
//
//        return userRepository.existsByUserId(userId);
//    }

}
