package com.danielvflores.writook.services;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.UserService;

public class RegisterUserTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "password";
        String displayName = "New User";

        User expectedUser = new User(username, null, email, password, displayName, "", "");
        
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        when(userService.createUser(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(expectedUser);

        // Act
        User registeredUser = authService.register(username, email, password, displayName);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        assertEquals(displayName, registeredUser.getDisplayName());
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        // Arrange
        String username = "existinguser";
        String email = "existinguser@example.com";
        String password = "password";
        String displayName = "Existing User";

        User existingUser = new User(username, null, email, password, displayName, "", "");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(existingUser));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(username, email, password, displayName);
        });
    }
}