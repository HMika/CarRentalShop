package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.exception.rental.RentalCreationException;
import com.rental.CarRentalShop.exception.rental.RentalNotFoundException;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

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

    @BeforeEach
    void setUp() {
        rental = Rental.builder()
                .id(1L)
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();

        rentalDTO = RentalDTO.builder()
                .id(1L)
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 7))
                .isPaid(true)
                .build();
    }

    @Test
    void shouldGetAllRentals() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getAllRentals();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    void shouldGetRentalById() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.getRentalById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(rentalRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenRentalNotFound() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentalService.getRentalById(99L))
                .isInstanceOf(RentalNotFoundException.class)
                .hasMessage("Rental not found with ID: 99");
    }

    @Test
    void shouldCreateRental() {
        rentalDTO.setCar(CarDTO.builder().id(10L).build());
        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));

        when(rentalMapper.toEntity(rentalDTO)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.createRental(rentalDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void shouldUpdateRental() {
        rentalDTO.setCar(CarDTO.builder().id(123L).build());
        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));
        rentalDTO.setIsPaid(false);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.updateRental(1L, rentalDTO);

        assertThat(result).isNotNull();
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingRentalWithInvalidDateRange() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalDTO.setCar(CarDTO.builder().id(100L).build());
        rentalDTO.setStartDate(LocalDate.of(2024, 3, 10));
        rentalDTO.setEndDate(LocalDate.of(2024, 3, 1));

        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        assertThatThrownBy(() -> rentalService.updateRental(1L, rentalDTO))
                .isInstanceOf(RentalCreationException.class)
                .hasMessageContaining("Invalid date range");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingRental() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentalService.updateRental(99L, rentalDTO))
                .isInstanceOf(RentalNotFoundException.class)
                .hasMessage("Rental not found with ID: 99");
    }

    @Test
    void shouldDeleteRental() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.deleteRental(1L);

        verify(rentalRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingRental() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentalService.deleteRental(99L))
                .isInstanceOf(RentalNotFoundException.class)
                .hasMessage("Rental not found with ID: 99");
    }

    @Test
    void shouldGetRentalsByUser() {
        when(rentalRepository.findByUserId(1L)).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getRentalsByUser(1L);

        assertThat(result).hasSize(1);
        verify(rentalRepository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldGetRentalsByCar() {
        when(rentalRepository.findByCarId(1L)).thenReturn(List.of(rental));
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        List<RentalDTO> result = rentalService.getRentalsByCar(1L);

        assertThat(result).hasSize(1);
        verify(rentalRepository, times(1)).findByCarId(1L);
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

    //    @Test
//    void shouldThrowExceptionWhenUpdatingRentalWithOverlappingDates() {
//        // We already have a rental in DB
//        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
//
//        // Simulate user changes car to ID=99, and date range overlaps
//        rentalDTO.setCar(CarDTO.builder().id(99L).build());
//        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
//        rentalDTO.setEndDate(LocalDate.of(2024, 3, 10));
//
//        // The service calls `rentalMapper.toDTO(rental)` to validate
//        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);
//
//        // Return a modifiable list for overlapping rentals
//        Rental overlap = new Rental();
//        when(rentalRepository.findOverlappingRentalsForCar(eq(99L), any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(new ArrayList<>(List.of(overlap)));
//        // ^^ Instead of List.of(overlap)
//
//        // Expect a RentalCreationException
//        assertThatThrownBy(() -> rentalService.updateRental(1L, rentalDTO))
//                .isInstanceOf(RentalCreationException.class)
//                .hasMessageContaining("Car is not available");
//    }
    @Test
    void shouldThrowExceptionWhenUpdatingRentalWithOverlappingDates() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        // Simulate user changes car to ID=99, and date range overlaps
        rentalDTO.setCar(CarDTO.builder().id(99L).build());
        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
        rentalDTO.setEndDate(LocalDate.of(2024, 3, 10));

        // The service calls `rentalMapper.toDTO(rental)` to validate
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        // Create an overlapping rental **with an ID**
        Rental overlap = Rental.builder()
                .id(2L) // Ensuring ID is set
                .startDate(LocalDate.of(2024, 3, 5))
                .endDate(LocalDate.of(2024, 3, 10))
                .build();

        // Return a list with the overlapping rental
        when(rentalRepository.findOverlappingRentalsForCar(eq(99L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>(List.of(overlap)));

        // Expect a RentalCreationException
        assertThatThrownBy(() -> rentalService.updateRental(1L, rentalDTO))
                .isInstanceOf(RentalCreationException.class)
                .hasMessageContaining("Car is not available");
    }

//    @Test
//    void shouldThrowExceptionWhenCarIsNotAvailable() {
//        // For creation test
//        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
//        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));
//        rentalDTO.setCar(CarDTO.builder().id(1L).build());
//
//        // Mock the overlap
//        Rental overlappingRental = new Rental();
//        when(rentalRepository.findOverlappingRentalsForCar(eq(1L), any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(new ArrayList<>(List.of(overlappingRental)));
//        // ^^ Instead of List.of(overlappingRental)
//
//        // Expect creation to fail
//        assertThatThrownBy(() -> rentalService.createRental(rentalDTO))
//                .isInstanceOf(RentalCreationException.class)
//                .hasMessageContaining("Car is not available");
//    }

    @Test
    void shouldThrowExceptionWhenCarIsNotAvailable() {
        // For creation test
        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));
        rentalDTO.setCar(CarDTO.builder().id(1L).build());

        // Mock an overlapping rental with a valid ID
        Rental overlappingRental = Rental.builder()
                .id(2L) // ✅ Setting a valid ID to prevent NullPointerException
                .startDate(LocalDate.of(2024, 3, 3)) // Overlaps
                .endDate(LocalDate.of(2024, 3, 6))
                .build();

        // Mock the repository to return an overlapping rental
        when(rentalRepository.findOverlappingRentalsForCar(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>(List.of(overlappingRental)));

        // Expect creation to fail due to car unavailability
        assertThatThrownBy(() -> rentalService.createRental(rentalDTO))
                .isInstanceOf(RentalCreationException.class)
                .hasMessageContaining("Car is not available");
    }


}
//    @Test
//    void shouldThrowExceptionWhenUpdatingRentalWithOverlappingDates() {
//        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
//
//        rentalDTO.setCar(CarDTO.builder().id(99L).build());
//        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
//        rentalDTO.setEndDate(LocalDate.of(2024, 3, 10));
//
//        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);
//
//        // Force an overlap
//        Rental overlap = new Rental();
//        when(rentalRepository.findOverlappingRentalsForCar(eq(99L), any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(List.of(overlap));
//
//        assertThatThrownBy(() -> rentalService.updateRental(1L, rentalDTO))
//                .isInstanceOf(RentalCreationException.class)
//                .hasMessageContaining("Car is not available");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCarIsNotAvailable() {
//        rentalDTO.setStartDate(LocalDate.of(2024, 3, 1));
//        rentalDTO.setEndDate(LocalDate.of(2024, 3, 5));
//
//
//        CarDTO carDTO = CarDTO.builder().id(1L).build();
//        rentalDTO.setCar(carDTO);
//
//        Rental overlappingRental = new Rental();
//        when(rentalRepository.findOverlappingRentalsForCar(eq(1L), any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(List.of(overlappingRental));
//
//        assertThatThrownBy(() -> rentalService.createRental(rentalDTO))
//                .isInstanceOf(RentalCreationException.class)
//                .hasMessageContaining("Car is not available");
//    }
//}