package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Role;
import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(roleMapper::toDTO).orElse(null);
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        if (!roleRepository.existsById(id)) {
            return null;
        }
        Role role = roleMapper.toEntity(roleDTO);
        role.setId(id);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
