package com.rental.CarRentalShop.mapper;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final RoleMapper roleMapper;
    private final RentalMapper rentalMapper;

    @Autowired
    public UserMapper(RoleMapper roleMapper, @Lazy RentalMapper rentalMapper) {
        this.roleMapper = roleMapper;
        this.rentalMapper = rentalMapper;
    }

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .contactInfo(user.getContactInfo())
                .password(user.getPassword())
                .role(roleMapper.toDTO(user.getRole()))
                .rentals(user.getRentals() != null
                        ? user.getRentals().stream().map(rentalMapper::toDTO).collect(Collectors.toList())
                        : null)
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) return null;

        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .contactInfo(userDTO.getContactInfo())
                .password(userDTO.getPassword())
                .role(roleMapper.toEntity(userDTO.getRole()))
                .rentals(userDTO.getRentals() != null
                        ? userDTO.getRentals().stream().map(rentalMapper::toEntity).collect(Collectors.toSet())
                        : null)
                .build();
    }
}
