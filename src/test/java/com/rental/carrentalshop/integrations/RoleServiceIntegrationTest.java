package com.rental.carrentalshop.integrations;

import com.rental.carrentalshop.dto.RoleDTO;
import com.rental.carrentalshop.exception.role.RoleNotFoundException;
import com.rental.carrentalshop.service.RoleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class RoleServiceIntegrationTest {

    @Autowired
    private RoleService roleService;

    @Test
    @Order(1)
    void shouldCreateAndRetrieveRole() {
        // Given
        RoleDTO roleDTO = RoleDTO.builder().roleName("Manager").build();

        // When
        RoleDTO savedRole = roleService.createRole(roleDTO);
        RoleDTO retrievedRole = roleService.getRoleById(savedRole.getId());

        // Then
        assertThat(savedRole).isNotNull();
        assertThat(retrievedRole).isNotNull();
        assertThat(retrievedRole.getRoleName()).isEqualTo("Manager");
    }

    @Test
    @Order(2)
    void shouldRetrieveAllRoles() {
        // Given
        roleService.createRole(RoleDTO.builder().roleName("Editor").build());
        roleService.createRole(RoleDTO.builder().roleName("Viewer").build());

        // When
        List<RoleDTO> roles = roleService.getAllRoles();

        // Then
        assertThat(roles.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Order(3)
    void shouldUpdateRole() {
        // Given
        RoleDTO roleDTO = roleService.createRole(RoleDTO.builder().roleName("Assistant").build());
        RoleDTO updatedRoleDTO = RoleDTO.builder().id(roleDTO.getId()).roleName("Senior Assistant").build();

        // When
        RoleDTO updatedRole = roleService.updateRole(roleDTO.getId(), updatedRoleDTO);

        // Then
        assertThat(updatedRole).isNotNull();
        assertThat(updatedRole.getRoleName()).isEqualTo("Senior Assistant");
    }

    @Test
    @Order(4)
    void shouldDeleteRole() {
        // Given
        RoleDTO roleDTO = roleService.createRole(RoleDTO.builder().roleName("Support").build());
        Long roleId = roleDTO.getId();

        // When
        roleService.deleteRole(roleId);

        // Then - Expect `RoleNotFoundException`
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(roleId));
    }
}
