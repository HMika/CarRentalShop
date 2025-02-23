package com.rental.CarRentalShop.integrations;

import com.rental.CarRentalShop.dto.RoleDTO;
import com.rental.CarRentalShop.dto.UserDTO;
import com.rental.CarRentalShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private UserDTO userDTO;
    private RoleDTO roleDTO;


//    @BeforeEach
//    void setUp() {
//        String uniqueUsername = "testuser" + System.currentTimeMillis(); // Avoids conflicts
//
//        roleDTO = RoleDTO.builder()
//                .id(1L)
//                .roleName("User")
//                .build();
//
//        userDTO = UserDTO.builder()
//                .username("TestUser"+ System.currentTimeMillis())
//                .name("Test")
//                .surname("User")
//                .contactInfo("test@example.com"+ System.currentTimeMillis())
//                .password("password123")
//                .role(roleDTO)
//                .build();
//    }
//
//    @Test
//    @Transactional
//    void shouldCreateAndRetrieveUser() {
//        UserDTO savedUser = userService.createUser(userDTO);
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getUsername()).isEqualTo(userDTO.getUsername());
//
//        UserDTO retrievedUser = userService.getUserById(savedUser.getId());
//        assertThat(retrievedUser).isNotNull();
//        assertThat(retrievedUser.getUsername()).isEqualTo(userDTO.getUsername());
//    }
//
//    @Test
//    void shouldUpdateUser() {
//        UserDTO savedUser = userService.createUser(userDTO);
//        savedUser.setName("UpdatedName");
//        UserDTO updatedUser = userService.updateUser(savedUser.getId(), savedUser);
//
//        assertThat(updatedUser).isNotNull();
//        assertThat(updatedUser.getName()).isEqualTo("UpdatedName");
//    }
//
//    @Test
//    void shouldDeleteUser() {
//        UserDTO savedUser = userService.createUser(userDTO);
//        userService.deleteUser(savedUser.getId());
//        List<UserDTO> users = userService.getAllUsers();
//        assertThat(users).isEmpty();
//    }
//
//    @Test
//    void shouldGetUserByUsername() {
//        UserDTO savedUser = userService.createUser(userDTO);
//        UserDTO foundUser = userService.getUserByUsername("testuser");
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUsername()).isEqualTo("testuser");
//    }
}
