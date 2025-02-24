package com.rental.CarRentalShop.exception.car;

public class DuplicateCarException extends RuntimeException {
    public DuplicateCarException(String registrationNumber) {
        super("Car with registration number " + registrationNumber + " already exists.");
    }
}
