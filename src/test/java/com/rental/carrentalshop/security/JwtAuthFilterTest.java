package com.rental.carrentalshop.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtUtil);
    }

    @Test
    void shouldAuthenticateUserWhenTokenIsValid() throws ServletException, IOException {
        String token = "valid-token";
        String username = "testuser";
        String role = "USER";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(username);
        when(jwtUtil.extractRole(token)).thenReturn(role);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(1)).validateToken(token);
        verify(jwtUtil, times(1)).extractRole(token);
        verify(filterChain, times(1)).doFilter(request, response);

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert authenticatedUser.getUsername().equals(username);
    }

    @Test
    void shouldNotAuthenticateWhenNoTokenIsProvided() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(0)).validateToken(anyString());
        verify(jwtUtil, times(0)).extractRole(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void shouldNotAuthenticateWhenTokenFormatIsInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(0)).validateToken(anyString());
        verify(jwtUtil, times(0)).extractRole(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}

