package com.example.travelappbackend.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="user_info")

public class User {

    @Id
    public String id;

    public String userId;

    private String password;

    private String phone;

}

