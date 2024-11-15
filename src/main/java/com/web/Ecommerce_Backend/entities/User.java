package com.web.Ecommerce_Backend.entities;


import com.web.Ecommerce_Backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "userTable")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String name;
    private String email;
    private String password;

    private UserRole role;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;
}
