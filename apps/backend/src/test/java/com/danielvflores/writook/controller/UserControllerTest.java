package com.danielvflores.writook.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.danielvflores.writook.model.User;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userController.getAllUsers();
        
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("danielvflores", users.get(0).getUsername());
        assertEquals("janedoe", users.get(1).getUsername());
        assertEquals("johnsmith", users.get(2).getUsername());
    }

    @Test
    void testGetUserById() {
        User user = userController.getUserById(1L);
        
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("danielvflores", user.getUsername());
        assertEquals("daniel@email.com", user.getEmail());
    }

    @Test
    void testGetUserByIdNotFound() {
        User user = userController.getUserById(999L);
        
        assertNull(user);
    }

    @Test
    void testCreateUser() {
        User newUser = new User("testuser", null, "test@email.com", "hashedPassword", "Test User", "Test bio", "http://test.url");
        
        User createdUser = userController.createUser(newUser);
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@email.com", createdUser.getEmail());
        
        // Verify that the user was added
        List<User> users = userController.getAllUsers();
        assertEquals(4, users.size());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User("danielupdated", null, "daniel.updated@email.com", "newHashedPassword", "Daniel Updated", "Updated bio", "http://updated.url");
        
        User result = userController.updateUser(1L, updatedUser);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("danielupdated", result.getUsername());
        assertEquals("daniel.updated@email.com", result.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        User updatedUser = new User("nonexistent", null, "none@email.com", "password", "None", "None", "none");
        
        User result = userController.updateUser(999L, updatedUser);
        
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        String result = userController.deleteUser(1L);
        
        assertEquals("User with ID 1 deleted successfully", result);
        
        // Verify the user was deleted
        User deletedUser = userController.getUserById(1L);
        assertNull(deletedUser);
        
        List<User> users = userController.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserProfile() {
        User userProfile = userController.getUserProfile(1L);
        
        assertNotNull(userProfile);
        assertEquals(1L, userProfile.getId());
        assertEquals("danielvflores", userProfile.getUsername());
    }
}