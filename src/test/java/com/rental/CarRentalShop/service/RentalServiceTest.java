package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.exception.rental.RentalNotFoundException;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDTO(rental)).thenReturn(rentalDTO);

        RentalDTO result = rentalService.updateRental(1L, rentalDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(rentalRepository, times(1)).save(rental);
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
}