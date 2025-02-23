package com.rental.CarRentalShop.repository;

import com.rental.CarRentalShop.domain.Car;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    private Car car1;
    private Car car2;


    public String setUp() {
        String regNumberTimestamp = String.valueOf(System.currentTimeMillis());

        car1 = Car.builder()
                .make("Toyota")
                .model("Camry")
                .year(2020)
                .registrationNumber(regNumberTimestamp)
                .rentalPrice(new BigDecimal("49.99"))
                .build();

        car2 = Car.builder()
                .make("Honda")
                .model("Accord")
                .year(2021)
                .registrationNumber(regNumberTimestamp + "1")
                .rentalPrice(new BigDecimal("59.99"))
                .build();

        return regNumberTimestamp;
    }

    @Test
    public void testAddCar() {
        setUp();
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(7);
    }

    @Test
    public void testRemoveCar() {
        String regNumber = setUp();

        carRepository.save(car1);
        carRepository.save(car2);

        carRepository.delete(car1);

        List<Car> cars = carRepository.findAll();

        assertThat(cars).hasSize(6);
        assertThat(cars.get(5).getRegistrationNumber()).isEqualTo(regNumber + "1");
    }

    @Test
    public void testGetAllCars() {
        String regNumber = setUp();
        // Save both cars
        carRepository.save(car1);
        carRepository.save(car2);

        // Retrieve all cars and verify their registration numbers
        List<Car> cars = carRepository.findAll();
        List<String> actualRegNumbers = cars.stream()
                .map(Car::getRegistrationNumber)
                .collect(Collectors.toList());

        List<String> expectedRegNumbers = Arrays.asList("ABC123", "DEF456", "GHI789", "JKL012", "MNO345", regNumber, regNumber + "1");

        assertThat(actualRegNumbers)
                .containsExactlyInAnyOrderElementsOf(expectedRegNumbers);
    }
}
