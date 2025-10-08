package com.danielvflores.writook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.ApiResponseDTO;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.UserService;

@RestController
@RequestMapping("/api/v1/debug")
public class DebugController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponseDTO> listUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponseDTO response = new ApiResponseDTO(true, "OK", users);
        return ResponseEntity.ok(response);
    }

}
