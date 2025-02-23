package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .name("Test")
                .surname("User")
                .contactInfo("test@example.com")
                .password("securepassword")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .name("Test")
                .surname("User")
                .contactInfo("test@example.com")
                .password("securepassword")
                .build();
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with ID: 99");
    }

    @Test
    void shouldGetUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUsername("unknownuser"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with username: unknownuser");
    }

    @Test
    void shouldCreateUser() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleMapper.toEntity(userDTO.getRole())).thenReturn(user.getRole());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, userDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with ID: 99");
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with ID: 99");
    }
}