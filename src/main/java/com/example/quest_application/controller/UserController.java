package com.example.quest_application.controller;

import com.example.quest_application.entity.User;
import com.example.quest_application.exception.BadRequestException;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.security.AuthenticationResponse;
import com.example.quest_application.security.JwtUtil;
import com.example.quest_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Kullanıcı kaydı
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userService.registerUser(user, Set.of("USER")); // Varsayılan rol atanıyor
            return ResponseEntity.ok(savedUser);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Kullanıcı girişi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User existingUser = userService.getUserByUsername(user.getUsername());
            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                String token = jwtUtil.generateToken(existingUser.getUsername());
                return ResponseEntity.ok(new AuthenticationResponse(token));
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // ID ile kullanıcı getir
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Kullanıcı sil
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
