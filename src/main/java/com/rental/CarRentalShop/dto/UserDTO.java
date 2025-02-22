package com.rental.CarRentalShop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String contactInfo;
    private String password;
    private RoleDTO role;
    private List<RentalDTO> rentals;
}
