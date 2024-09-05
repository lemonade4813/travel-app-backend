package com.example.travelappbackend.controller;


import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.ResponseDTO;
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
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {

        try {
            userService.addUser(userDTO);
            ResponseDTO response = ResponseDTO.builder().message("성공적으로 회원가입이 완료되었습니다.").build();
            return ResponseEntity.ok().body(response);
        } catch (ResponseStatusException e) {

            ErrorDTO error = null;

            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                error = ErrorDTO.builder().error("이미 가입된 아이디입니다.").build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            else{
                error = ErrorDTO.builder().error("오류가 발생하였습니다.").build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }
}
