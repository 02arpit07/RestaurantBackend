package com.example.restaurantapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Transient
    private String rawPassword;

    private String password;

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        this.password = passwordEncoder().encode(rawPassword);
    }

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Add getters and setters
}