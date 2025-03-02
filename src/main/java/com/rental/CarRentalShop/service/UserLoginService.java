package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserLoginRequest;
import com.rental.CarRentalShop.dto.UserLoginResponse;
import com.rental.CarRentalShop.exception.user.UserNotFoundException;
import com.rental.CarRentalShop.repository.UserRepository;
import com.rental.CarRentalShop.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserLoginService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserLoginResponse authenticateUser(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return new UserLoginResponse(user.getId(), user.getUsername(), "Login successful", token);
    }
}

