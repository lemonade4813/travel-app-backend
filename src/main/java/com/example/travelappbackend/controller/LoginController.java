package com.example.travelappbackend.controller;

import com.example.travelappbackend.model.ErrorDTO;
import com.example.travelappbackend.model.LoginDTO;
import com.example.travelappbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController

public class LoginController {

        @Autowired
        private LoginService loginService;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
            try {
                // LoginService에서 로그인 처리 및 토큰 생성
                String token = loginService.login(loginDTO);

                Map<String, Object> accessToken = new HashMap<>();

                accessToken.put("access_token", token);

                // 토큰을 담아서 프론트엔드로 리턴
                return ResponseEntity.ok().body(accessToken);
            } catch (RuntimeException e) {
                // 예외 발생 시 에러 메시지와 함께 401 Unauthorized 반환
                ErrorDTO error = ErrorDTO.builder().error(e.getMessage()).build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
        }

}
