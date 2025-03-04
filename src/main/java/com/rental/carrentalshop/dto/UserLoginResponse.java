package com.rental.carrentalshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private Long userId;
    private String username;
    private String message;
    private String token;
}
