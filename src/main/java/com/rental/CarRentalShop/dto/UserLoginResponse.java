package com.rental.CarRentalShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private Long userId;
    private String username;
    private String message;
}
