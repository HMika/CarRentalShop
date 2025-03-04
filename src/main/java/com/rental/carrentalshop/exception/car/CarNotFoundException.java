package com.rental.carrentalshop.exception.car;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long id) {
        super("Car with ID " + id + " not found.");
    }
}
