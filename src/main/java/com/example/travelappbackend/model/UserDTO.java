package com.example.travelappbackend.model;


import com.example.travelappbackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String userId;

    private String password;

    private String phone;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .build();
    }
}
