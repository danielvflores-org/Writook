package com.danielvflores.writook.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.danielvflores.writook.dto.RegisterRequestDTO;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthCompleteTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;

	public AuthCompleteTest() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	void testRegister() throws Exception {
		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setUsername("testuser");
		request.setEmail("test@email.com");
		request.setPassword("password123");
		request.setDisplayName("Test User");

		User mockUser = new User("testuser", 1L, "test@email.com", "password123", "Test User", "", "");
		when(authService.register("testuser", "test@email.com", "password123", "Test User")).thenReturn(mockUser);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testuser"));
	}
}
