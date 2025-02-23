package com.rental.CarRentalShop.integrations;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.repository.CarRepository;
import com.rental.CarRentalShop.service.CarService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CarServiceIntegrationTest {
    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    private CarDTO carDTO;

    @BeforeEach
    void setUp() {
        carDTO = CarDTO.builder()
                .make("Toyota")
                .model("Corolla")
                .year(2022)
                .registrationNumber("ABC-123")
                .rentalPrice(BigDecimal.valueOf(50.00))
                .build();
    }

    @Test
    void shouldCreateAndRetrieveCar() {
        // 1. Create Car
        CarDTO savedCar = carService.createCar(carDTO);
        assertNotNull(savedCar.getId());
        assertEquals("Toyota", savedCar.getMake());

        // 2. Retrieve the same car from DB
        Optional<Car> retrievedCar = carRepository.findById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        assertEquals("Toyota", retrievedCar.get().getMake());
    }

    @Test
    void shouldUpdateCar() {
        // 1. Create Car
        CarDTO savedCar = carService.createCar(carDTO);
        assertNotNull(savedCar.getId());

        // 2. Update Car
        CarDTO updatedCarDTO = CarDTO.builder()
                .id(savedCar.getId())
                .make("Honda")
                .model("Civic")
                .year(2023)
                .registrationNumber("XYZ-789")
                .rentalPrice(BigDecimal.valueOf(60.00))
                .build();

        CarDTO updatedCar = carService.updateCar(savedCar.getId(), updatedCarDTO);
        assertNotNull(updatedCar);
        assertEquals("Honda", updatedCar.getMake());

        // 3. Verify in DB
        Optional<Car> retrievedCar = carRepository.findById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        assertEquals("Honda", retrievedCar.get().getMake());
    }

    @Test
    void shouldDeleteCar() {
        // 1. Create Car
        CarDTO savedCar = carService.createCar(carDTO);
        assertNotNull(savedCar.getId());

        // 2. Delete Car
        carService.deleteCar(savedCar.getId());

        // 3. Verify deletion
        Optional<Car> deletedCar = carRepository.findById(savedCar.getId());
        assertFalse(deletedCar.isPresent());
    }
}
