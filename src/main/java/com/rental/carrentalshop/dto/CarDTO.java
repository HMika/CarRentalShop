package com.rental.carrentalshop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CarDTO {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String registrationNumber;
    private BigDecimal rentalPrice;
}
