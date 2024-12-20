package com.example.quest_application.service;

import com.example.quest_application.entity.User;
import com.example.quest_application.exception.BadRequestException;
import com.example.quest_application.exception.ResourceNotFoundException;
import com.example.quest_application.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Kullanıcı kaydı
    public User registerUser(User user, Set<String> roles) {
        logger.info("Attempting to register a new user with username: {}", user.getUsername());

        if (user.getUsername() == null || user.getPassword() == null) {
            logger.error("Username or password is null. Registration failed.");
            throw new BadRequestException("Username and password cannot be null.");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("Username '{}' is already taken. Registration failed.", user.getUsername());
            throw new BadRequestException("Username is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        logger.info("User successfully registered with ID: {}", savedUser.getId());
        return savedUser;
    }

    // ID ile kullanıcı getir
    public User getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);

        return userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new ResourceNotFoundException("User not found with id: " + id);
        });
    }

    // Kullanıcı adına göre kullanıcı getir
    public User getUserByUsername(String username) {
        logger.info("Fetching user with username: {}", username);

        return userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found with username: {}", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });
    }

    // Kullanıcıyı sil
    public void deleteUser(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new ResourceNotFoundException("User not found with id: " + id);
        });

        userRepository.delete(user);
        logger.info("User successfully deleted with ID: {}", id);
    }
}
