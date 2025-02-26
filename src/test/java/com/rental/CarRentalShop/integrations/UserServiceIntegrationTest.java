package com.rental.CarRentalShop.integrations;

import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.exception.user.UserNotFoundException;
import com.rental.CarRentalShop.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private UserDTO userDTO;
    private RoleDTO roleDTO;
    private String dynamicUsername;

    @BeforeEach
    void setUp() {
        dynamicUsername = "testuser" + System.currentTimeMillis(); // Avoids conflicts

        roleDTO = RoleDTO.builder()
                .id(1L)
                .roleName("User")
                .build();

        userDTO = UserDTO.builder()
                .username(dynamicUsername)
                .name("Test")
                .surname("User")
                .contactInfo("test@example.com" + System.currentTimeMillis())
                .password("password123")
                .role(roleDTO)
                .build();
    }

    @Test
    @Transactional
    @Order(1)
    void shouldCreateAndRetrieveUser() {
        // Create User
        UserDTO savedUser = userService.createUser(userDTO);

        // Assertions for creation
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(userDTO.getUsername());

        // Retrieve User
        UserDTO retrievedUser = userService.getUserById(savedUser.getId());

        // Assertions for retrieval
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    @Transactional
    @Order(2)
    void shouldUpdateUser() {
        // Create Initial User
        UserDTO savedUser = userService.createUser(userDTO);
        assertNotNull(savedUser.getId());

        UserDTO freshUser = UserDTO.builder()
                .id(savedUser.getId())
                .username("alexandros")
                .name("alex")
                .surname("novak")
                .contactInfo("alecs@example.com" + System.currentTimeMillis())
                .password("asf1mkwrnf")
                .role(roleDTO)
                .build();

        // Update User
        UserDTO updatedUser = userService.updateUser(savedUser.getId(), freshUser);

        // Assertions for update
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getName(), "alex");  // Match the actual updated name
    }

    @Test
    @Transactional
    @Order(3)
    void shouldDeleteUser() {
        // Create User
        UserDTO savedUser = userService.createUser(userDTO);

        // Delete User
        userService.deleteUser(savedUser.getId());

        // Verify User is Deleted by Expecting Exception
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    @Transactional
    @Order(4)
    void shouldGetUserByUsername() {
        // Create User
        userService.createUser(userDTO);

        // Retrieve by Username
        UserDTO foundUser = userService.getUserByUsername(dynamicUsername);

        // Assertions
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(dynamicUsername);
    }

    @Test
    @Transactional
    @Order(5)
    void shouldThrowExceptionWhenUserNotFoundById() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    @Transactional
    @Order(6)
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonexistentUser"));
    }
}