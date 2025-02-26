package com.rental.CarRentalShop.exception.role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role with ID " + id + " not found.");
    }
}
