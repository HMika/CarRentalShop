package com.rental.CarRentalShop.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordHashedTest {
    @Test
    public void test() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String[] plainPasswords = {"password123", "securepass", "adminpass", "michaelpass", "sarahpass"};

        for (String password : plainPasswords) {
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(password + " -> " + hashedPassword);
        }
    }
}
