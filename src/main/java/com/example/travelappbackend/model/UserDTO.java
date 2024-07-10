package com.example.travelappbackend.model;


import com.example.travelappbackend.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDTO {

    private String userId;

    private String password;

    private Integer phone;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .build();
    }
}
