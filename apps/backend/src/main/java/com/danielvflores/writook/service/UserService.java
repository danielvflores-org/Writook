package com.danielvflores.writook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danielvflores.writook.model.User;

@Service
public class UserService {

    // Business logic related to users will be implemented here
    // MOCK STRUCTURES FOR DEVELOPMENT PURPOSES
    private final List<User> users = new ArrayList<>();
    private Long currentId = 1L;

    public UserService() {
        // MY MOCK DATA IS PROVISIONAL FOR DEVELOPMENT PURPOSES
        // THIS MOCK DATA WILL BE REMOVED ONCE CONNECTED TO A REAL DATABASE
        users.add(new User("danielvflores", 1L, "daniel@email.com", "hashedPassword123", "Daniel V. Flores", "A passionate writer and developer.", "https://avatar.url/daniel.jpg"));
        users.add(new User("janedoe", 2L, "jane@email.com", "hashedPassword456", "Jane Doe", "Love writing fantasy stories!", "https://avatar.url/jane.jpg"));
        users.add(new User("johnsmith", 3L, "john@email.com", "hashedPassword789", "John Smith", "Sci-fi enthusiast and coder.", "https://avatar.url/john.jpg"));
        currentId = 4L;
    }

    public List<User> getAllUsers() {
        return users;
    }
    
    public User getUserById(Long id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public User createUser(User user) {
        user.setId(currentId++);
        users.add(user);
        return user;
    }
    
    public User updateUser(Long id, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                updatedUser.setId(id);
                users.set(i, updatedUser);
                return updatedUser;
            }
        }
        return null;
    }
    
    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
    
    public User getUserProfile(Long id) {
        return getUserById(id);
    }
}
