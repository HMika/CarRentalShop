package com.rental.CarRentalShop.exception.car;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long id) {
        super("Car with ID " + id + " not found.");
    }
}
