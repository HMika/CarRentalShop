package com.rental.CarRentalShop.controller;


import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        logger.info("Fetching all rentals.");
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        logger.info("Fetching rental with ID: {}", id);
        RentalDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PostMapping
    public ResponseEntity<RentalDTO> createRental(@RequestBody RentalDTO rentalDTO) {
        logger.info("Creating new rental.");
        RentalDTO createdRental = rentalService.createRental(rentalDTO);
        return ResponseEntity.ok(createdRental);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(@PathVariable Long id, @RequestBody RentalDTO rentalDTO) {
        logger.info("Updating rental with ID: {}", id);
        RentalDTO updatedRental = rentalService.updateRental(id, rentalDTO);
        return ResponseEntity.ok(updatedRental);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        logger.info("Deleting rental with ID: {}", id);
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDTO>> getRentalsByUser(@PathVariable Long userId) {
        logger.info("Fetching rentals for user ID: {}", userId);
        List<RentalDTO> rentals = rentalService.getRentalsByUser(userId);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<RentalDTO>> getRentalsByCar(@PathVariable Long carId) {
        logger.info("Fetching rentals for car ID: {}", carId);
        List<RentalDTO> rentals = rentalService.getRentalsByCar(carId);
        return ResponseEntity.ok(rentals);
    }
}
