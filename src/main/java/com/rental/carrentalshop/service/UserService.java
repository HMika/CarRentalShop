package com.rental.carrentalshop.service;

import com.rental.carrentalshop.domain.User;
import com.rental.carrentalshop.dto.UserDTO;
import com.rental.carrentalshop.exception.user.UserAlreadyExistsException;
import com.rental.carrentalshop.exception.user.UserNotFoundException;
import com.rental.carrentalshop.mapper.RoleMapper;
import com.rental.carrentalshop.mapper.UserMapper;
import com.rental.carrentalshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final String placeholder = "*******";


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
        logger.info("Retrieved {} users", users.size());
        users.forEach(user -> user.setPassword(placeholder));
        return users;
    }

    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", id);
                    return new UserNotFoundException(id);
                });

        logger.info("User found: {}", user.getUsername());
        user.setPassword(placeholder);
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
        user.setPassword(placeholder);
        return userMapper.toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user with username: {}", userDTO.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.error("User with username '{}' already exists", userDTO.getUsername());
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }

        // Hash the password before storing
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        savedUser.setPassword(placeholder);
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found, cannot update", id);
                    return new UserNotFoundException(id);
                });

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setName(userDTO.getName());
        existingUser.setSurname(userDTO.getSurname());
        existingUser.setContactInfo(userDTO.getContactInfo());
        existingUser.setRole(roleMapper.toEntity(userDTO.getRole()));

        // Only update the password if a new one is provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            logger.info("Updating password for user ID: {}", id);
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        logger.info("User with ID {} updated successfully", id);
        updatedUser.setPassword(placeholder);
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
