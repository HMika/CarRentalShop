package com.rental.CarRentalShop.exception.role;


public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(Long rentalId) {
        super("Rental not found with ID: " + rentalId);
    }
}
