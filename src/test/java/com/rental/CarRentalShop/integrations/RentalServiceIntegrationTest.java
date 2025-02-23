package com.rental.CarRentalShop.integrations;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RentalServiceIntegrationTest {
//    @Autowired
//    private RentalService rentalService;
//
//    @Autowired
//    private RentalRepository rentalRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CarRepository carRepository;
//
//    @Autowired
//    private RentalMapper rentalMapper;
//
//    private User testUser;
//    private Car testCar;
//
//    @BeforeEach
//    void setUp() {
//        // Create and save test user
//        testUser = User.builder()
//                .username("john_doe")
//                .password("securepass")
//                .name("John")
//                .surname("Doe")
//                .contactInfo("john.doe@example.com")
//                .build();
//        userRepository.save(testUser);
//
//        // Create and save test car
//        testCar = Car.builder()
//                .make("Toyota")
//                .model("Corolla")
//                .year(2020)
//                .registrationNumber("ABC-123")
//                .rentalPrice(BigDecimal.valueOf(50.00))
//                .build();
//        carRepository.save(testCar);
//    }
//
//    @Test
//    @Order(1)
//    void shouldCreateAndRetrieveRental() {
//        // Create RentalDTO
//        RentalDTO rentalDTO = RentalDTO.builder()
//                .user(rentalMapper.toDTO(testUser))
//                .car(rentalMapper.toDTO(testCar))
//                .startDate(LocalDate.of(2024, 3, 1))
//                .endDate(LocalDate.of(2024, 3, 7))
//                .isPaid(true)
//                .build();
//
//        // Create rental
//        RentalDTO createdRental = rentalService.createRental(rentalDTO);
//        assertThat(createdRental).isNotNull();
//        assertThat(createdRental.getId()).isNotNull();
//
//        // Retrieve rental
//        RentalDTO fetchedRental = rentalService.getRentalById(createdRental.getId());
//        assertThat(fetchedRental).isNotNull();
//        assertThat(fetchedRental.getUser().getId()).isEqualTo(testUser.getId());
//        assertThat(fetchedRental.getCar().getId()).isEqualTo(testCar.getId());
//    }
//
//    @Test
//    @Order(2)
//    void shouldUpdateRental() {
//        // Create RentalDTO
//        Rental rental = Rental.builder()
//                .user(testUser)
//                .car(testCar)
//                .startDate(LocalDate.of(2024, 3, 1))
//                .endDate(LocalDate.of(2024, 3, 7))
//                .isPaid(true)
//                .build();
//        rentalRepository.save(rental);
//
//        // Update rental
//        RentalDTO updatedRentalDTO = rentalMapper.toDTO(rental);
//        updatedRentalDTO.setEndDate(LocalDate.of(2024, 3, 10));
//
//        RentalDTO updatedRental = rentalService.updateRental(rental.getId(), updatedRentalDTO);
//
//        // Verify the update
//        assertThat(updatedRental).isNotNull();
//        assertThat(updatedRental.getEndDate()).isEqualTo(LocalDate.of(2024, 3, 10));
//    }
//
//    @Test
//    @Order(3)
//    void shouldDeleteRental() {
//        // Create RentalDTO
//        Rental rental = Rental.builder()
//                .user(testUser)
//                .car(testCar)
//                .startDate(LocalDate.of(2024, 3, 1))
//                .endDate(LocalDate.of(2024, 3, 7))
//                .isPaid(true)
//                .build();
//        rentalRepository.save(rental);
//
//        // Delete rental
//        rentalService.deleteRental(rental.getId());
//
//        // Verify deletion
//        Optional<Rental> deletedRental = rentalRepository.findById(rental.getId());
//        assertThat(deletedRental).isEmpty();
//    }
//
//    @Test
//    @Order(4)
//    void shouldRetrieveRentalsByUser() {
//        // Create and save rentals
//        Rental rental1 = Rental.builder()
//                .user(testUser)
//                .car(testCar)
//                .startDate(LocalDate.of(2024, 3, 1))
//                .endDate(LocalDate.of(2024, 3, 7))
//                .isPaid(true)
//                .build();
//        rentalRepository.save(rental1);
//
//        Rental rental2 = Rental.builder()
//                .user(testUser)
//                .car(testCar)
//                .startDate(LocalDate.of(2024, 3, 10))
//                .endDate(LocalDate.of(2024, 3, 15))
//                .isPaid(false)
//                .build();
//        rentalRepository.save(rental2);
//
//        // Fetch by user ID
//        List<RentalDTO> userRentals = rentalService.getRentalsByUser(testUser.getId());
//        assertThat(userRentals).hasSize(2);
//    }
}
