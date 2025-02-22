package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Role;
import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = Role.builder().id(3L).roleName("Manager").build();
        roleDTO = RoleDTO.builder().id(3L).roleName("Manager").build();
    }

    @Test
    void getAllRoles_ShouldReturnRoleDTOList() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        List<RoleDTO> roles = roleService.getAllRoles();

        assertEquals(1, roles.size());
        assertEquals("Manager", roles.get(0).getRoleName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_ShouldReturnRoleDTO() {
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO foundRole = roleService.getRoleById(3L);

        assertNotNull(foundRole);
        assertEquals("Manager", foundRole.getRoleName());
        verify(roleRepository, times(1)).findById(3L);
    }

    @Test
    void getRoleById_ShouldReturnNull_WhenRoleNotFound() {
        when(roleRepository.findById(4L)).thenReturn(Optional.empty());

        RoleDTO foundRole = roleService.getRoleById(4L);

        assertNull(foundRole);
        verify(roleRepository, times(1)).findById(4L);
    }

    @Test
    void createRole_ShouldReturnSavedRoleDTO() {
        when(roleMapper.toEntity(roleDTO)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO savedRole = roleService.createRole(roleDTO);

        assertNotNull(savedRole);
        assertEquals("Manager", savedRole.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void updateRole_ShouldReturnUpdatedRoleDTO_WhenRoleExists() {
        when(roleRepository.existsById(3L)).thenReturn(true);
        when(roleMapper.toEntity(roleDTO)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO updatedRole = roleService.updateRole(3L, roleDTO);

        assertNotNull(updatedRole);
        assertEquals("Manager", updatedRole.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void updateRole_ShouldReturnNull_WhenRoleDoesNotExist() {
        when(roleRepository.existsById(4L)).thenReturn(false);

        RoleDTO updatedRole = roleService.updateRole(4L, roleDTO);

        assertNull(updatedRole);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void deleteRole_ShouldCallRepositoryDeleteById() {
        doNothing().when(roleRepository).deleteById(3L);

        roleService.deleteRole(3L);

        verify(roleRepository, times(1)).deleteById(3L);
    }
}
