package com.rental.CarRentalShop.Integration;


import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void addUserToUserTable() {
        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setEmail("new_user@example.com");

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("new_user");
        assertThat(savedUser.getEmail()).isEqualTo("new_user@example.com");
    }
}
