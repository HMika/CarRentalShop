package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.mapper.UserMapperDec;
import com.rental.CarRentalShop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        assertNotNull(dataSource);
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapperDec userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    void canConnectToDatabase() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void getAllUsersReturnsListOfUserDTOs() {
        User john = new User();
        john.setUsername("john_doe");
        john.setEmail("john@example.com");
        User jane = new User();
        jane.setUsername("jane_doe");
        jane.setEmail("jane@example.com");
        User michael = new User();
        michael.setUsername("michael_smith");
        michael.setEmail("michael@example.com");

        UserDTO johnDTO = new UserDTO();
        johnDTO.setUsername("john_doe");
        johnDTO.setEmail("john@example.com");
        UserDTO janeDTO = new UserDTO();
        janeDTO.setUsername("jane_doe");
        janeDTO.setEmail("jane@example.com");
        UserDTO michaelDTO = new UserDTO();
        michaelDTO.setUsername("michael_smith");
        michaelDTO.setEmail("michael@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(john, jane, michael));
        when(userMapper.toDTO(john)).thenReturn(johnDTO);
        when(userMapper.toDTO(jane)).thenReturn(janeDTO);
        when(userMapper.toDTO(michael)).thenReturn(michaelDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(johnDTO);
        assertThat(result.get(1)).isEqualTo(janeDTO);
        assertThat(result.get(2)).isEqualTo(michaelDTO);
    }

    @Test
    void getUserByIdReturnsUserDTO() {
        User john = new User();
        john.setUsername("john_doe");
        john.setEmail("john@example.com");
        UserDTO johnDTO = new UserDTO();
        johnDTO.setUsername("john_doe");
        johnDTO.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(john));
        when(userMapper.toDTO(john)).thenReturn(johnDTO);

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isEqualTo(johnDTO);
    }

    @Test
    void getUserByIdThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(4L));
    }

    @Test
    void createUserSavesAndReturnsUserDTO() {
        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setEmail("new@example.com");
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUsername("new_user");
        newUserDTO.setEmail("new@example.com");

        when(userMapper.toEntity(newUserDTO)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toDTO(newUser)).thenReturn(newUserDTO);

        UserDTO result = userService.createUser(newUserDTO);

        assertThat(result).isEqualTo(newUserDTO);
    }

    @Test
    void updateUserUpdatesAndReturnsUserDTO() {
        User existingUser = new User();
        existingUser.setUsername("john_doe");
        existingUser.setEmail("john@example.com");
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUsername("john_doe_updated");
        updatedUserDTO.setEmail("john_updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toDTO(existingUser)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertThat(result).isEqualTo(updatedUserDTO);
    }

    @Test
    void updateUserThrowsExceptionWhenUserNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("non_existent_user");
        userDTO.setEmail("non_existent@example.com");

        when(userRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(4L, userDTO));
    }

    @Test
    void deleteUserThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(4L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(4L));
    }
}