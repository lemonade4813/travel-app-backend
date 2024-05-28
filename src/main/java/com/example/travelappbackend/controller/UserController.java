package com.example.travelappbackend.controller;


import com.example.travelappbackend.model.UserDTO;
import com.example.travelappbackend.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/user")
    public String addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return "성공적으로 회원가입이 완료되었습니다.";
    }

    @GetMapping("/userIdCheck")
    public ResponseEntity<?> userIdCheck(@RequestParam String userId) throws BadRequestException {

        System.out.println(userId);

        if(userService.existsByMemberId(userId)){
            throw new BadRequestException("이미 사용중인 아이디입니다.");
        }
        else{
            return ResponseEntity.ok("사용가능한 아이디 입니다.");
        }
    }
}
