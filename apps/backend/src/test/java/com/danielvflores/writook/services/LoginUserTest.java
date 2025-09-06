package com.danielvflores.writook.services;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.UserService;

public class LoginUserTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String username = "danielvflores";
        String password = "password123";
        
        User existingUser = new User(username, 1L, "daniel@email.com", password, "Daniel V. Flores", "A passionate writer.", "https://avatar.url/daniel.jpg");
        
        when(userService.getAllUsers()).thenReturn(Arrays.asList(existingUser));

        // Act
        User loggedInUser = authService.login(username, password);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals("daniel@email.com", loggedInUser.getEmail());
        assertEquals("Daniel V. Flores", loggedInUser.getDisplayName());
    }

    @Test
    public void testLogin_InvalidUsername() {
        // Arrange
        String username = "nonexistentuser";
        String password = "password123";
        
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act
        User loggedInUser = authService.login(username, password);

        // Assert
        assertNull(loggedInUser);
    }

    @Test
    public void testLogin_InvalidPassword() {
        // Arrange
        String username = "danielvflores";
        String incorrectPassword = "wrongpassword";
        String correctPassword = "password123";
        
        User existingUser = new User(username, 1L, "daniel@email.com", correctPassword, "Daniel V. Flores", "A passionate writer.", "https://avatar.url/daniel.jpg");
        
        when(userService.getAllUsers()).thenReturn(Arrays.asList(existingUser));

        // Act
        User loggedInUser = authService.login(username, incorrectPassword);

        // Assert
        assertNull(loggedInUser);
    }

    @Test
    public void testLogin_EmptyUserList() {
        // Arrange
        String username = "anyuser";
        String password = "anypassword";
        
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act
        User loggedInUser = authService.login(username, password);

        // Assert
        assertNull(loggedInUser);
    }
}