package com.rental.CarRentalShop.integrations;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.domain.Role;
import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.CarRepository;
import com.rental.CarRentalShop.repository.RentalRepository;
import com.rental.CarRentalShop.repository.RoleRepository;
import com.rental.CarRentalShop.repository.UserRepository;
import com.rental.CarRentalShop.service.RentalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RentalServiceIntegrationTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarMapper carMapper;

    private User testUser;
    private Car testCar;

    @BeforeEach
    void setUp() {
        Role userRole = roleRepository.findByRoleName("User")
                .orElseThrow(() -> new RuntimeException("Role 'User' not found!"));

        testUser = User.builder()
                .username("john_doe_test")
                .password("securepass")
                .name("John")
                .surname("Doe")
                .contactInfo("john.doe.test@example.com")
                .role(userRole)
                .build();
        userRepository.save(testUser);

        testCar = Car.builder()
                .make("Toyota")
                .model("Corolla")
                .year(2020)
                .registrationNumber("TEST-123") // Unique
                .rentalPrice(BigDecimal.valueOf(50.00))
                .build();
        carRepository.save(testCar);
    }


    @Test
    @Order(1)
    void shouldCreateAndRetrieveRental() {
        RentalDTO rentalDTO = RentalDTO.builder()
                .user(userMapper.toDTO(testUser))
                .car(carMapper.toDTO(testCar))
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();

        // Create rental
        RentalDTO createdRental = rentalService.createRental(rentalDTO);
        assertThat(createdRental).isNotNull();
        assertThat(createdRental.getId()).isNotNull();

        // Retrieve rental
        RentalDTO fetchedRental = rentalService.getRentalById(createdRental.getId());
        assertThat(fetchedRental).isNotNull();
        assertThat(fetchedRental.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(fetchedRental.getCar().getId()).isEqualTo(testCar.getId());
    }

    @Test
    @Order(2)
    void shouldUpdateRental() {
        // Create & save initial Rental entity
        Rental rental = Rental.builder()
                .user(testUser)
                .car(testCar)
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();
        rentalRepository.save(rental);

        // Convert to DTO for update
        RentalDTO toUpdate = rentalMapper.toDTO(rental);
        toUpdate.setEndDate(LocalDate.of(2024, 3, 10));

        // Perform update
        RentalDTO updatedRental = rentalService.updateRental(rental.getId(), toUpdate);
        assertThat(updatedRental).isNotNull();
        assertThat(updatedRental.getEndDate()).isEqualTo(LocalDate.of(2024, 3, 10));
    }

    @Test
    @Order(3)
    void shouldDeleteRental() {
        // Create & save initial Rental entity
        Rental rental = Rental.builder()
                .user(testUser)
                .car(testCar)
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();
        rentalRepository.save(rental);

        // Delete rental
        rentalService.deleteRental(rental.getId());

        // Verify deletion
        Optional<Rental> deleted = rentalRepository.findById(rental.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @Order(4)
    void shouldRetrieveRentalsByUser() {
        // Create & save multiple rentals for the same user
        Rental rental1 = Rental.builder()
                .user(testUser)
                .car(testCar)
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();
        rentalRepository.save(rental1);

        Rental rental2 = Rental.builder()
                .user(testUser)
                .car(testCar)
                .startDate(LocalDate.of(2024, 3, 10))
                .endDate(LocalDate.of(2024, 3, 15))
                .isPaid(false)
                .build();
        rentalRepository.save(rental2);

        // Fetch by user ID
        List<RentalDTO> userRentals = rentalService.getRentalsByUser(testUser.getId());
        assertThat(userRentals).hasSize(2);
    }
}
