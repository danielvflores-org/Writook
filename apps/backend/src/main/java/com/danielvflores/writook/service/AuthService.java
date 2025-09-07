package com.danielvflores.writook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
        return null;
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userService.findByUsername(username);
    }
    return null;
    }
}
