package com.rental.CarRentalShop.controller;

import com.rental.CarRentalShop.dto.UserLoginRequest;
import com.rental.CarRentalShop.dto.UserLoginResponse;
import com.rental.CarRentalShop.security.JwtUtil;
import com.rental.CarRentalShop.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserLoginController {
    private final UserLoginService userLoginService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserLoginController(UserLoginService userLoginService, JwtUtil jwtUtil) {
        this.userLoginService = userLoginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userLoginService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String jwt = token.substring(7); // Remove "Bearer " prefix
        String username = jwtUtil.validateToken(jwt);

        if (username == null) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        return ResponseEntity.ok("Token is valid for user: " + username);
    }
}