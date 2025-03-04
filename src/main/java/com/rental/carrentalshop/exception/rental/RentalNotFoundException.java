package com.rental.carrentalshop.exception.rental;


public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(Long rentalId) {
        super("Rental not found with ID: " + rentalId);
    }
}
