package com.example.restaurantapp.service;

import com.example.restaurantapp.dao.UserRepository;
import com.example.restaurantapp.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        user.setRawPassword(user.getRawPassword());
        return userRepository.save(user);
    }

    public ResponseEntity<String> login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            String token = generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    public void logout() {
        // Implement logout logic if needed
    }

    private String generateToken(User user) {
        String secretKey = "your_secret_key";

        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}