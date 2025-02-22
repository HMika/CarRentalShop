package com.rental.CarRentalShop.mapper;

import com.rental.CarRentalShop.domain.Role;
import com.rental.CarRentalShop.dto.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDTO(Role role) {
        if (role == null) return null;

        return RoleDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) return null;

        return Role.builder()
                .id(roleDTO.getId())
                .roleName(roleDTO.getRoleName())
                .build();
    }
}
