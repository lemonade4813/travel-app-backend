package com.example.travelappbackend.controller;


import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {

        try {
            userService.addUser(userDTO);
            return ResponseEntity.ok("성공적으로 회원가입이 완료되었습니다.");
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 아이디입니다.");
            }
            throw e;
        }
    }
}
