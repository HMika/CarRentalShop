package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Role;
import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.exception.rental.RoleAlreadyExistsException;
import com.rental.CarRentalShop.exception.rental.RoleNotFoundException;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDTO> getAllRoles() {
        logger.info("Fetching all roles");
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} roles", roles.size());
        return roles;
    }

    public RoleDTO getRoleById(Long id) {
        logger.info("Fetching role with ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Role with ID {} not found", id);
                    return new RoleNotFoundException(id);
                });

        logger.info("Role found: {}", role);
        return roleMapper.toDTO(role);
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        logger.info("Creating a new role: {}", roleDTO);

        // Check if role with same name already exists
        Optional<Role> existingRole = roleRepository.findAll().stream()
                .filter(role -> role.getRoleName().equalsIgnoreCase(roleDTO.getRoleName()))
                .findFirst();

        if (existingRole.isPresent()) {
            logger.error("Role with name '{}' already exists", roleDTO.getRoleName());
            throw new RoleAlreadyExistsException("Role with name '" + roleDTO.getRoleName() + "' already exists.");
        }

        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        logger.info("Role created successfully with ID: {}", savedRole.getId());
        return roleMapper.toDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        logger.info("Updating role with ID: {}", id);

        if (!roleRepository.existsById(id)) {
            logger.error("Role with ID {} not found, cannot update", id);
            throw new RoleNotFoundException(id);
        }

        Role role = roleMapper.toEntity(roleDTO);
        role.setId(id);
        Role updatedRole = roleRepository.save(role);
        logger.info("Role with ID {} updated successfully", id);
        return roleMapper.toDTO(updatedRole);
    }

    public void deleteRole(Long id) {
        logger.info("Deleting role with ID: {}", id);

        if (!roleRepository.existsById(id)) {
            logger.error("Role with ID {} not found, cannot delete", id);
            throw new RoleNotFoundException(id);
        }

        roleRepository.deleteById(id);
        logger.info("Role with ID {} deleted successfully", id);
    }

}
