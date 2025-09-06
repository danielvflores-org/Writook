package com.danielvflores.writook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.model.User;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    // Basicall Logic for Auth Provisionally - without hashing method for passwords
    public User login(String username, String password) {
        User user = userService.getAllUsers().stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User register(String username, String email, String password, String displayName) {
        if (userExists(username)) {
            throw new RuntimeException("User already exists");
        }
        
        // Create user WITHOUT hashing password (provisional)
        User newUser = new User(username, null, email, password, displayName, "", "");
        return userService.createUser(newUser);
    }
    
    private boolean userExists(String username) {
        return userService.getAllUsers().stream()
            .anyMatch(u -> u.getUsername().equals(username));
    }
    
}
