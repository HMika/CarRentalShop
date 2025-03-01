package com.rental.CarRentalShop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RentalDTO {
    private Long id;
    private UserDTO user;
    private CarDTO car;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isPaid;
    private BigDecimal totalPrice;
}
