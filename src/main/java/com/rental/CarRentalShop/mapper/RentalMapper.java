package com.rental.CarRentalShop.mapper;

import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.dto.RentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
    private final UserMapper userMapper;
    private final CarMapper carMapper;

    @Autowired
    public RentalMapper(UserMapper userMapper, CarMapper carMapper) {
        this.userMapper = userMapper;
        this.carMapper = carMapper;
    }

    public RentalDTO toDTO(Rental rental) {
        if (rental == null) return null;

        return RentalDTO.builder()
                .id(rental.getId())
                .user(userMapper.toDTO(rental.getUser()))
                .car(carMapper.toDTO(rental.getCar()))
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .isPaid(rental.getIsPaid())
                .totalPrice(rental.getTotalPrice())
                .build();
    }

    public Rental toEntity(RentalDTO rentalDTO) {
        if (rentalDTO == null) return null;

        return Rental.builder()
                .id(rentalDTO.getId())
                .user(userMapper.toEntity(rentalDTO.getUser()))
                .car(carMapper.toEntity(rentalDTO.getCar()))
                .startDate(rentalDTO.getStartDate())
                .endDate(rentalDTO.getEndDate())
                .isPaid(rentalDTO.getIsPaid())
                .totalPrice(rentalDTO.getTotalPrice())
                .build();
    }
}
