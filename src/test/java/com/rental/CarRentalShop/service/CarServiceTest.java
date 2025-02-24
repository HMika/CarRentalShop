package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarService carService;

    private Car car;
    private CarDTO carDTO;

    @BeforeEach
    void setUp() {
        car = Car.builder()
                .id(1L)
                .make("Toyota")
                .model("Corolla")
                .year(2022)
                .registrationNumber("ABC-123")
                .rentalPrice(BigDecimal.valueOf(50.00))
                .build();

        carDTO = CarDTO.builder()
                .id(1L)
                .make("Toyota")
                .model("Corolla")
                .year(2022)
                .registrationNumber("ABC-123")
                .rentalPrice(BigDecimal.valueOf(50.00))
                .build();
    }

    @Test
    void getAllCars_ShouldReturnCarDTOList() {
        when(carRepository.findAll()).thenReturn(Arrays.asList(car));
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        List<CarDTO> cars = carService.getAllCars();

        assertEquals(1, cars.size());
        assertEquals("Toyota", cars.get(0).getMake());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_ShouldReturnCarDTO() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        CarDTO foundCar = carService.getCarById(1L);

        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getMake());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void getCarById_ShouldReturnNull_WhenCarNotFound() {
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        CarDTO foundCar = carService.getCarById(2L);

        assertNull(foundCar);
        verify(carRepository, times(1)).findById(2L);
    }

    @Test
    void createCar_ShouldReturnSavedCarDTO() {
        when(carMapper.toEntity(carDTO)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        CarDTO savedCar = carService.createCar(carDTO);

        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getMake());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void updateCar_ShouldReturnUpdatedCarDTO_WhenCarExists() {
        when(carRepository.existsById(1L)).thenReturn(true);
        when(carMapper.toEntity(carDTO)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        CarDTO updatedCar = carService.updateCar(1L, carDTO);

        assertNotNull(updatedCar);
        assertEquals("Toyota", updatedCar.getMake());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void updateCar_ShouldReturnNull_WhenCarDoesNotExist() {
        when(carRepository.existsById(2L)).thenReturn(false);

        CarDTO updatedCar = carService.updateCar(2L, carDTO);

        assertNull(updatedCar);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteCar_ShouldCallDeleteById_WhenCarExists() {
        // Arrange
        when(carRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1L);

        // Act
        carService.deleteCar(1L);

        // Assert
        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCar_ShouldNotCallDeleteById_WhenCarDoesNotExist() {
        // Arrange
        when(carRepository.existsById(2L)).thenReturn(false);

        // Act
        carService.deleteCar(2L);

        // Assert
        verify(carRepository, times(1)).existsById(2L);
        verify(carRepository, never()).deleteById(anyLong());
    }

}