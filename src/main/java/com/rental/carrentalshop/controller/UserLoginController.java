package com.rental.carrentalshop.controller;

import com.rental.carrentalshop.dto.UserLoginRequest;
import com.rental.carrentalshop.dto.UserLoginResponse;
import com.rental.carrentalshop.security.JwtUtil;
import com.rental.carrentalshop.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userLoginService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String jwt = token.substring(7);
        String username = jwtUtil.validateToken(jwt);

        if (username == null) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        return ResponseEntity.ok("Token is valid for user: " + username);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String jwt = token.substring(7);
        String username = jwtUtil.validateToken(jwt);

        if (username != null) {
            jwtUtil.revokeUserToken(username); // Revoke user’s token
            return ResponseEntity.ok("User logged out successfully, token revoked.");
        }

        return ResponseEntity.status(400).body("Invalid or expired token.");
    }

    @PostMapping("/logout-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> logoutByUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }

        jwtUtil.revokeUserToken(username);
        return ResponseEntity.ok("User " + username + " has been logged out.");
    }

    @PostMapping("/logout-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> logoutAllUsers() {
        jwtUtil.revokeAllTokens();
        return ResponseEntity.ok("All users have been logged out, all tokens revoked.");
    }
}
