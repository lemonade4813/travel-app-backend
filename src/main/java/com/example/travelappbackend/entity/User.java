package com.example.travelappbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user_info")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String userId;

    private String password;

    private Integer phone;

}

