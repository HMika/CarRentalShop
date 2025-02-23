package com.rental.CarRentalShop.mapper;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.dto.CarDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CarMapper {
    public CarDTO toDTO(Car car) {
        if (car == null) return null;

        return CarDTO.builder()
                .id(car.getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getYear())
                .registrationNumber(car.getRegistrationNumber())
                .rentalPrice(BigDecimal.valueOf(car.getRentalPrice().doubleValue()))
                .build();
    }

    public Car toEntity(CarDTO carDTO) {
        if (carDTO == null) return null;

        return Car.builder()
                .id(carDTO.getId())
                .make(carDTO.getMake())
                .model(carDTO.getModel())
                .year(carDTO.getYear())
                .registrationNumber(carDTO.getRegistrationNumber())
                .rentalPrice(BigDecimal.valueOf(carDTO.getRentalPrice().doubleValue()))
                .build();
    }
}
