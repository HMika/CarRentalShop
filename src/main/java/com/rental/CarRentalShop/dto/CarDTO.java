package com.rental.CarRentalShop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDTO {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String registrationNumber;
    private Double rentalPrice;
}
