package com.rental.CarRentalShop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RentalDTO {
    private Long id;
    private UserDTO user;
    private CarDTO car;
    private Date startDate;
    private Date endDate;
    private Boolean isPaid;
}
