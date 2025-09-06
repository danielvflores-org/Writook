package com.danielvflores.writook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.model.User;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // MOCK STRUCTURES FOR DEVELOPMENT PURPOSES
    private final List<User> users = new ArrayList<>();
    private Long currentId = 4L;
    
    public UserController() {
        // MY MOCK DATA IS PROVISIONAL FOR DEVELOPMENT PURPOSES
        // THIS MOCK DATA WILL BE REMOVED ONCE CONNECTED TO A REAL DATABASE
        users.add(new User("danielvflores", 1L, "daniel@email.com", "hashedPassword123", "Daniel V. Flores", "A passionate writer and developer.", "https://avatar.url/daniel.jpg"));
        users.add(new User("janedoe", 2L, "jane@email.com", "hashedPassword456", "Jane Doe", "Love writing fantasy stories!", "https://avatar.url/jane.jpg"));
        users.add(new User("johnsmith", 3L, "john@email.com", "hashedPassword789", "John Smith", "Sci-fi enthusiast and coder.", "https://avatar.url/john.jpg"));
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(currentId++);
        users.add(user);
        return user;
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                updatedUser.setId(id);
                users.set(i, updatedUser);
                return updatedUser;
            }
        }
        return null;
    }
    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        users.removeIf(user -> user.getId().equals(id));
        return "User with ID " + id + " deleted successfully";
    }
    
    @GetMapping("/{id}/profile")
    public User getUserProfile(@PathVariable Long id) {
        return getUserById(id);
    }
}