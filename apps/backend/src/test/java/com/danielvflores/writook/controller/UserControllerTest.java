package com.danielvflores.writook.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.UserService;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange - Crear datos mock
        User user1 = new User("danielvflores", null, "daniel@email.com", "password", "Daniel", "", "");
        User user2 = new User("janedoe", null, "jane@email.com", "password", "Jane", "", "");
        User user3 = new User("johnsmith", null, "john@email.com", "password", "John", "", "");
        List<User> mockUsers = Arrays.asList(user1, user2, user3);
        
        when(userService.getAllUsers()).thenReturn(mockUsers);
        
        // Act
        List<User> users = userController.getAllUsers();
        
        // Assert
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("danielvflores", users.get(0).getUsername());
        assertEquals("janedoe", users.get(1).getUsername());
        assertEquals("johnsmith", users.get(2).getUsername());
    }

    @Test
    void testGetUserById() {
        // Arrange
        String testId = "test-uuid-123";
        User mockUser = new User("danielvflores", testId, "daniel@email.com", "password", "Daniel", "", "");
        when(userService.getUserById(testId)).thenReturn(mockUser);
        
        // Act
        User user = userController.getUserById(testId);
        
        // Assert
        assertNotNull(user);
        assertEquals(testId, user.getId());
        assertEquals("danielvflores", user.getUsername());
        assertEquals("daniel@email.com", user.getEmail());
    }

    @Test
    void testGetUserByIdNotFound() {
        // Arrange
        String nonExistentId = "non-existent-uuid";
        when(userService.getUserById(nonExistentId)).thenReturn(null);
        
        // Act
        User user = userController.getUserById(nonExistentId);
        
        // Assert
        assertNull(user);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User("testuser", null, "test@email.com", "hashedPassword", "Test User", "Test bio", "http://test.url");
        User createdUser = new User("testuser", "created-uuid-456", "test@email.com", "hashedPassword", "Test User", "Test bio", "http://test.url");
        
        when(userService.createUser(newUser)).thenReturn(createdUser);
        
        // Act
        User result = userController.createUser(newUser);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@email.com", result.getEmail());
        
        // Verify service method was called
        verify(userService).createUser(newUser);
    }

    @Test
    void testUpdateUser() {
        // Arrange
        String testId = "update-test-uuid";
        User updatedUser = new User("danielupdated", null, "daniel.updated@email.com", "newHashedPassword", "Daniel Updated", "Updated bio", "http://updated.url");
        User returnedUser = new User("danielupdated", testId, "daniel.updated@email.com", "newHashedPassword", "Daniel Updated", "Updated bio", "http://updated.url");
        
        when(userService.updateUser(testId, updatedUser)).thenReturn(returnedUser);
        
        // Act
        User result = userController.updateUser(testId, updatedUser);
        
        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals("danielupdated", result.getUsername());
        assertEquals("daniel.updated@email.com", result.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        // Arrange
        String nonExistentId = "non-existent-update-uuid";
        User updatedUser = new User("nonexistent", null, "none@email.com", "password", "None", "None", "none");
        when(userService.updateUser(nonExistentId, updatedUser)).thenReturn(null);
        
        // Act
        User result = userController.updateUser(nonExistentId, updatedUser);
        
        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        String testId = "delete-test-uuid";
        when(userService.deleteUser(testId)).thenReturn(true);
        
        // Act
        String result = userController.deleteUser(testId);
        
        // Assert
        assertEquals("User with ID " + testId + " deleted successfully", result);
        
        // Verify service method was called
        verify(userService).deleteUser(testId);
    }

    @Test
    void testGetUserProfile() {
        // Arrange
        String testId = "profile-test-uuid";
        User mockUser = new User("danielvflores", testId, "daniel@email.com", "password", "Daniel", "", "");
        when(userService.getUserProfile(testId)).thenReturn(mockUser);
        
        // Act
        User userProfile = userController.getUserProfile(testId);
        
        // Assert
        assertNotNull(userProfile);
        assertEquals(testId, userProfile.getId());
        assertEquals("danielvflores", userProfile.getUsername());
    }
}