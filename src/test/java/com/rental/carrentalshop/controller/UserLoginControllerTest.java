package com.rental.carrentalshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.carrentalshop.dto.UserLoginRequest;
import com.rental.carrentalshop.dto.UserLoginResponse;
import com.rental.carrentalshop.security.JwtUtil;
import com.rental.carrentalshop.service.UserLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserLoginControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private UserLoginService userLoginService;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserLoginController userLoginController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userLoginController).build();
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("testuser", "password123");
        UserLoginResponse loginResponse = new UserLoginResponse(1L, "testuser", "Login successful", "mocked-jwt-token");

        when(userLoginService.authenticateUser(any(UserLoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void shouldValidateToken() throws Exception {
        when(jwtUtil.validateToken("valid-token")).thenReturn("testuser");

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid for user: testuser"));
    }

    @Test
    void shouldReturnErrorForInvalidToken() throws Exception {
        when(jwtUtil.validateToken("invalid-token")).thenReturn(null);

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or expired token"));
    }

    @Test
    void shouldLogoutUser() throws Exception {
        when(jwtUtil.validateToken("valid-token")).thenReturn("testuser");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged out successfully, token revoked."));

        verify(jwtUtil, times(1)).revokeUserToken("testuser");
    }

    @Test
    void shouldNotLogoutWithInvalidToken() throws Exception {
        when(jwtUtil.validateToken("invalid-token")).thenReturn(null);

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired token."));
    }

    @Test
    void shouldLogoutUserByUsername() throws Exception {
        Map<String, String> request = Collections.singletonMap("username", "testuser");

        mockMvc.perform(post("/api/auth/logout-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User testuser has been logged out."));

        verify(jwtUtil, times(1)).revokeUserToken("testuser");
    }

    @Test
    void shouldNotLogoutUserWithoutUsername() throws Exception {
        Map<String, String> request = Collections.singletonMap("username", "");

        mockMvc.perform(post("/api/auth/logout-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is required."));
    }

    @Test
    void shouldLogoutAllUsers() throws Exception {
        mockMvc.perform(post("/api/auth/logout-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All users have been logged out, all tokens revoked."));

        verify(jwtUtil, times(1)).revokeAllTokens();
    }
}

