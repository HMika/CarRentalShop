package com.rental.CarRentalShop.exception.car;

public class CarDeletionException extends RuntimeException {
    public CarDeletionException(Long id) {
        super("Car with ID " + id + " cannot be deleted due to active rentals.");
    }
}

