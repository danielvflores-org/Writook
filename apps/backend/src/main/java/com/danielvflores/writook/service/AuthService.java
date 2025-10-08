package com.danielvflores.writook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.model.User;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    // Basicall Logic for Auth Provisionally - without hashing method for passwords
    public User login(String usernameOrEmail, String password) {
        User user = userService.getAllUsers().stream()
            .filter(u -> u.getUsername().equals(usernameOrEmail) || u.getEmail().equals(usernameOrEmail))
            .findFirst()
            .orElse(null);
        boolean matches = false;
        if (user != null) {
            try {
                matches = passwordEncoder.matches(password, user.getPassword());
            } catch (Exception ex) {
                logger.warn("Error al comparar contraseñas para usuario {}: {}", usernameOrEmail, ex.getMessage());
            }
        }
        logger.info("Login attempt for {}: foundUser={}, storedHashPresent={}, matches={}", usernameOrEmail, user != null, user != null && user.getPassword() != null, matches);
        if (user != null && matches) {
            return user;
        }
        return null;
    }

    public User register(String username, String email, String password, String displayName) {
        if (userExists(username)) {
            throw new RuntimeException("User already exists");
        }
        
        // Hash password before storing
        String hashed = passwordEncoder.encode(password);
        // Create user with generated id (User constructor generates UUID if id==null)
        User newUser = new User(username, null, email, hashed, displayName, "", null);
        userService.createUser(newUser);
        return newUser;
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
            String usernameOrEmail = userDetails.getUsername();
            User user = userService.findByUsername(usernameOrEmail);
            if (user == null) {
                user = userService.findByEmail(usernameOrEmail);
            }
            return user;
        }
        return null;
    }
}
