//package com.example.travelappbackend.controller;
//
//
//import com.example.travelappbackend.model.LoginDTO;
//import com.example.travelappbackend.service.LoginService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//
//public class LoginController {
//
//    @Autowired
//    private LoginService loginService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
//        return loginService.login(loginDTO);
//    }
//}
