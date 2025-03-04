package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserLoginRequest;
import com.rental.CarRentalShop.dto.UserLoginResponse;
import com.rental.CarRentalShop.exception.user.UserNotFoundException;
import com.rental.CarRentalShop.repository.UserRepository;
import com.rental.CarRentalShop.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserLoginService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public UserLoginResponse authenticateUser(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getRoleName());

        return new UserLoginResponse(user.getId(), user.getUsername(), "Login successful", token);
    }
}
