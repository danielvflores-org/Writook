package com.danielvflores.writook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.ApiResponseDTO;
import com.danielvflores.writook.dto.LoginRequestDTO;
import com.danielvflores.writook.dto.RegisterRequestDTO;
import com.danielvflores.writook.dto.RegisterResponseDTO;
import com.danielvflores.writook.dto.UserResponseDTO;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.utility.TokenJWTUtility;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {

            User user = authService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getDisplayName()
            );

            UserResponseDTO userResponse = new UserResponseDTO(user);
            RegisterResponseDTO response = new RegisterResponseDTO(userResponse);
            ApiResponseDTO apiResponse = new ApiResponseDTO(true, "Usuario registrado exitosamente", response);
            
            return ResponseEntity.ok(apiResponse);

        } catch (RuntimeException e) {
            if (e.getMessage().equals("User already exists")) {
                ApiResponseDTO errorResponse = new ApiResponseDTO(false, "El usuario ya existe", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            ApiResponseDTO errorResponse = new ApiResponseDTO(false, "Error interno del servidor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@RequestBody LoginRequestDTO request) {

        User user = authService.login(request.getUsername(), request.getPassword());

        if (user == null) {
            ApiResponseDTO errorResponse = new ApiResponseDTO(false, "Credenciales inválidas", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        String token = TokenJWTUtility.generateToken(user.getUsername());
        
        // Create response with both token and user data
        UserResponseDTO userResponse = new UserResponseDTO(user);
        java.util.Map<String, Object> responseData = new java.util.HashMap<>();
        responseData.put("token", token);
        responseData.put("user", userResponse);
        
        ApiResponseDTO successResponse = new ApiResponseDTO(true, "Inicio de sesión exitoso", responseData);
        
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO> getCurrentUser() {

        User user = authService.getCurrentUser();
        
        if (user == null) {
            ApiResponseDTO errorResponse = new ApiResponseDTO(false, "Usuario no autenticado", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        UserResponseDTO userResponse = new UserResponseDTO(user);
        ApiResponseDTO successResponse = new ApiResponseDTO(true, "Usuario autenticado", userResponse);
        
        return ResponseEntity.ok(successResponse);
    }

}
