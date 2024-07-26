//package com.example.travelappbackend.service;
//
//import com.example.travelappbackend.model.LoginDTO;
//import com.example.travelappbackend.repository.user.UserRepository;
//import com.example.travelappbackend.entity.user.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class LoginService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtService jwtService;
//
//    public ResponseEntity<?> login(LoginDTO loginDTO) {
//        if (!userRepository.existsByUserId(loginDTO.getLoginId())) {
//            return ResponseEntity.status(400).body("회원가입이 필요합니다.");
//        }
//
//        User user = userRepository.findByUserId(loginDTO.getLoginId());
//        if (!user.getPassword().equals(loginDTO.getPassword())) {
//            return ResponseEntity.status(400).body("로그인 정보가 올바르지 않습니다");
//        }
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", user.getUserId());
//
//        String token = jwtService.create(claims, LocalDateTime.now().plusHours(2));
//
//        return ResponseEntity.ok(token);
//    }
//}