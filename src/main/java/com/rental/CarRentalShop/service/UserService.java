package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        if (user.getRentals() != null) {
            user.getRentals().size();
        }

        return userMapper.toDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return userMapper.toDTO(user);
    }

    // Create a new user
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setName(userDTO.getName());
                    existingUser.setSurname(userDTO.getSurname());
                    existingUser.setContactInfo(userDTO.getContactInfo());
                    existingUser.setPassword(userDTO.getPassword());
                    existingUser.setRole(roleMapper.toEntity(userDTO.getRole()));

                    User updatedUser = userRepository.save(existingUser);
                    return userMapper.toDTO(updatedUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
