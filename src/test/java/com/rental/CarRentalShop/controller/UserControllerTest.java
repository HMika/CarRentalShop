package com.rental.CarRentalShop.controller;

import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserDTO sampleUserDTO;

    @BeforeEach
    void setUp() {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(1L)
                .roleName("USER")
                .build();

        sampleUserDTO = UserDTO.builder()
                .id(1L)
                .username("johndoe")
                .name("John")
                .surname("Doe")
                .password("securepassword")
                .contactInfo("johndoe@example.com")
                .role(roleDTO)
                .build();
    }

    @Test
    void testGetAllUsers_ReturnsOkAndUserList() throws Exception {
        List<UserDTO> users = Collections.singletonList(sampleUserDTO);
        BDDMockito.given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(sampleUserDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(sampleUserDTO.getUsername())))
                .andExpect(jsonPath("$[0].name", is(sampleUserDTO.getName())))
                .andExpect(jsonPath("$[0].role.roleName", is(sampleUserDTO.getRole().getRoleName())));

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_ReturnsOkAndUser() throws Exception {
        BDDMockito.given(userService.getUserById(1L)).willReturn(sampleUserDTO);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUserDTO.getId().intValue()))
                .andExpect(jsonPath("$.username").value(sampleUserDTO.getUsername()))
                .andExpect(jsonPath("$.name").value(sampleUserDTO.getName()))
                .andExpect(jsonPath("$.role.roleName").value(sampleUserDTO.getRole().getRoleName()));

        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
    }

    @Test
    void testGetUserByUsername_ReturnsOkAndUser() throws Exception {
        BDDMockito.given(userService.getUserByUsername("johndoe")).willReturn(sampleUserDTO);

        mockMvc.perform(get("/api/users/username/{username}", "johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUserDTO.getId().intValue()))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.role.roleName").value("USER"));

        Mockito.verify(userService, Mockito.times(1)).getUserByUsername("johndoe");
    }

    @Test
    void testCreateUser_ReturnsOkAndCreatedUser() throws Exception {
        BDDMockito.given(userService.createUser(any(UserDTO.class))).willReturn(sampleUserDTO);

        String requestBody = """
                {
                  "username": "johndoe",
                  "name": "John",
                  "surname": "Doe",
                  "password": "securepassword",
                  "contactInfo": "johndoe@example.com",
                  "role": { "id": 1, "roleName": "USER" }
                }""";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleUserDTO.getId().intValue()))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.role.roleName").value("USER"));

        Mockito.verify(userService, Mockito.times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void testUpdateUser_ReturnsOkAndUpdatedUser() throws Exception {
        UserDTO updatedUser = UserDTO.builder()
                .id(1L)
                .username("john_doe_updated")
                .name("John")
                .surname("Doe")
                .password("newpassword")
                .contactInfo("updated@example.com")
                .role(sampleUserDTO.getRole())
                .build();

        BDDMockito.given(userService.updateUser(eq(1L), any(UserDTO.class))).willReturn(updatedUser);

        String requestBody = """
                {
                  "username": "john_doe_updated",
                  "name": "John",
                  "surname": "Doe",
                  "password": "newpassword",
                  "contactInfo": "updated@example.com",
                  "role": { "id": 1, "roleName": "USER" }
                }""";

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId().intValue()))
                .andExpect(jsonPath("$.username").value("john_doe_updated"))
                .andExpect(jsonPath("$.role.roleName").value("USER"));

        Mockito.verify(userService, Mockito.times(1))
                .updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    void testDeleteUser_ReturnsNoContent() throws Exception {
        BDDMockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }
}
