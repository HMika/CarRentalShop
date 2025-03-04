package com.rental.carrentalshop.service;

import com.rental.carrentalshop.domain.Car;
import com.rental.carrentalshop.domain.Rental;
import com.rental.carrentalshop.domain.User;
import com.rental.carrentalshop.dto.CarDTO;
import com.rental.carrentalshop.dto.RentalDTO;
import com.rental.carrentalshop.dto.UserDTO;
import com.rental.carrentalshop.exception.rental.RentalCreationException;
import com.rental.carrentalshop.exception.rental.RentalNotFoundException;
import com.rental.carrentalshop.mapper.CarMapper;
import com.rental.carrentalshop.mapper.RentalMapper;
import com.rental.carrentalshop.mapper.UserMapper;
import com.rental.carrentalshop.repository.CarRepository;
import com.rental.carrentalshop.repository.RentalRepository;
import com.rental.carrentalshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private CarMapper carMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RentalService rentalService;

    private Rental rental;
    private RentalDTO rentalDTO;
    private Car car;
    private CarDTO carDTO;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("testUser")
                .build();

        car = Car.builder()
                .id(1L)
                .make("Toyota")
                .model("Camry")
                .rentalPrice(new BigDecimal("50"))
                .build();

        carDTO = CarDTO.builder()
                .id(1L)
                .make("Toyota")
                .model("Camry")
                .rentalPrice(new BigDecimal("50"))
                .build();

        rental = Rental.builder()
                .id(1L)
                .user(user)
                .car(car)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .isPaid(false)
                .totalPrice(new BigDecimal("150"))
                .build();

        rentalDTO = RentalDTO.builder()
                .id(1L)
                .user(userDTO)
                .car(carDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .isPaid(false)
                .totalPrice(new BigDecimal("150"))
                .build();
    }

    @Test
    void getAllRentals_ShouldReturnListOfRentalDTOs() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(any(Rental.class))).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getAllRentals();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    void getRentalById_ShouldReturnRentalDTO_WhenRentalExists() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.getRentalById(1L);

        assertNotNull(result);
        assertEquals(rentalDTO.getId(), result.getId());
    }

    @Test
    void getRentalById_ShouldThrowException_WhenRentalNotFound() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.getRentalById(1L));
    }

    @Test
    void createRental_ShouldReturnRentalDTO_WhenValidRentalIsCreated() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(rentalMapper.toEntity(any(RentalDTO.class))).thenReturn(rental);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
        when(rentalMapper.toDTO(any(Rental.class))).thenReturn(rentalDTO);

        RentalDTO result = rentalService.createRental(rentalDTO);

        assertNotNull(result);
        assertEquals(rentalDTO.getId(), result.getId());
    }

    @Test
    void createRental_ShouldThrowException_WhenCarNotFound() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        RentalCreationException exception = assertThrows(RentalCreationException.class, () -> rentalService.createRental(rentalDTO));

        assertTrue(exception.getMessage().contains("Car with ID 1 not found"));
    }


    @Test
    void createRental_ShouldThrowRentalCreationException_WhenUserNotFound() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RentalCreationException exception = assertThrows(RentalCreationException.class,
                () -> rentalService.createRental(rentalDTO));

        assertTrue(exception.getMessage().contains("User with ID 1 not found"));
    }

//    @Test
//    void shouldUpdateRental() {
//        rentalDTO.setUser(UserDTO.builder().id(1L).username("testUser").build());
//        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
//        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));
//        rentalDTO.setIsPaid(false);
//
//        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(carRepository.findById(123L)).thenReturn(Optional.of(car));
//        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
//        when(rentalMapper.toDTO(any(Rental.class))).thenReturn(rentalDTO);
//
//        RentalDTO result = rentalService.updateRental(1L, rentalDTO);
//
//        assertNotNull(result);
//        verify(rentalRepository, times(1)).save(any(Rental.class));
//    }

    @Test
    void updateRental_ShouldThrowException_WhenRentalNotFound() {
        when(rentalRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.updateRental(1L, rentalDTO));
    }

    @Test
    void deleteRental_ShouldDeleteRental_WhenRentalExists() {
        when(rentalRepository.findById(anyLong())).thenReturn(Optional.of(rental));

        rentalService.deleteRental(1L);

        verify(rentalRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRental_ShouldThrowException_WhenRentalNotFound() {
        when(rentalRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.deleteRental(1L));
    }

    @Test
    void getRentalsByUser_ShouldReturnListOfRentalDTOs() {
        when(rentalRepository.findByUserId(1L)).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(any(Rental.class))).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getRentalsByUser(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getRentalsByCar_ShouldReturnListOfRentalDTOs() {
        when(rentalRepository.findByCarId(1L)).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(any(Rental.class))).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getRentalsByCar(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        RentalDTO invalidDateRentalDTO = RentalDTO.builder()
                .startDate(LocalDate.of(2024, 3, 10))
                .endDate(LocalDate.of(2024, 3, 1))
                .build();

        assertThatThrownBy(() -> rentalService.createRental(invalidDateRentalDTO))
                .isInstanceOf(RentalCreationException.class)
                .hasMessageContaining("Invalid date range");
    }

    @Test
    void shouldThrowExceptionWhenRentalDatesAreNull() {
        RentalDTO invalidDateRentalDTO = RentalDTO.builder()
                .startDate(null)
                .endDate(null)
                .build();
        assertThatThrownBy(() -> rentalService.createRental(invalidDateRentalDTO))
                .isInstanceOf(RentalCreationException.class)
                .hasMessageContaining("Rental dates cannot be null");
    }
}