package com.rental.CarRentalShop.exception.rental;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role with ID " + id + " not found.");
    }
}
