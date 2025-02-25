package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.exception.user.UserAlreadyExistsException;
import com.rental.CarRentalShop.exception.user.UserNotFoundException;
import com.rental.CarRentalShop.mapper.RoleMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        logger.info("Fetching all users");
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} users", users.size());
        return users;
    }

    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", id);
                    return new UserNotFoundException(id);
                });

        if (user.getRentals() != null) {
            user.getRentals().size(); // Force initialization if needed
        }

        logger.info("User found: {}", user.getUsername());
        return userMapper.toDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        logger.info("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User with username '{}' not found", username);
                    return new UserNotFoundException(username);
                });

        logger.info("User found: {}", user.getUsername());
        return userMapper.toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user with username: {}", userDTO.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.error("User with username '{}' already exists", userDTO.getUsername());
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found, cannot update", id);
                    return new UserNotFoundException(id);
                });

        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setContactInfo(userDTO.getContactInfo());
        user.setPassword(userDTO.getPassword());
        user.setRole(roleMapper.toEntity(userDTO.getRole()));

        User updatedUser = userRepository.save(user);
        logger.info("User with ID {} updated successfully", id);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            logger.error("User with ID {} not found, cannot delete", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        logger.info("User with ID {} deleted successfully", id);
    }
}
