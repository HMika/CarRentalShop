package com.rental.carrentalshop.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " not found.");
    }

    public UserNotFoundException(String username) {
        super("User with username '" + username + "' not found.");
    }
}
