package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.exception.rental.RentalCreationException;
import com.rental.CarRentalShop.exception.rental.RentalNotFoundException;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserMapper userMapper;
    private final CarMapper carMapper;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         RentalMapper rentalMapper,
                         UserMapper userMapper,
                         CarMapper carMapper) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.userMapper = userMapper;
        this.carMapper = carMapper;
    }

    private static <T> void setIfNotNull(Supplier<T> getter, Consumer<T> setter) {
        T value = getter.get();
        if (value != null) {
            setter.accept(value);
        }
    }

    /**
     * Fetch all rentals.
     */
    public List<RentalDTO> getAllRentals() {
        logger.info("Fetching all rentals");
        List<RentalDTO> rentals = rentalRepository.findAll()
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} rentals", rentals.size());
        return rentals;
    }

    /**
     * Get a single rental by ID.
     */
    public RentalDTO getRentalById(Long id) {
        logger.info("Fetching rental with ID: {}", id);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Rental with ID {} not found", id);
                    return new RentalNotFoundException(id);
                });
        logger.info("Found rental with ID: {}", id);
        return rentalMapper.toDTO(rental);
    }

    /**
     * Create a new rental.
     */
    public RentalDTO createRental(RentalDTO rentalDTO) {
        logger.info("Creating a new rental");

        validateNewRental(rentalDTO);

        try {
            Rental rentalEntity = rentalMapper.toEntity(rentalDTO);
            Rental savedRental = rentalRepository.save(rentalEntity);
            logger.info("Successfully created rental with ID: {}", savedRental.getId());
            return rentalMapper.toDTO(savedRental);
        } catch (Exception ex) {
            logger.error("Failed to create rental: {}", ex.getMessage());
            throw new RentalCreationException("Failed to create rental. Reason: " + ex.getMessage());
        }
    }

    /**
     * Update an existing rental by ID.
     */
    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        logger.info("Updating rental with ID: {}", id);

        Rental existing = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        setIfNotNull(rentalDTO::getUser, user -> existing.setUser(userMapper.toEntity(user)));
        setIfNotNull(rentalDTO::getCar, car -> existing.setCar(carMapper.toEntity(car)));
        setIfNotNull(rentalDTO::getStartDate, existing::setStartDate);
        setIfNotNull(rentalDTO::getEndDate, existing::setEndDate);
        setIfNotNull(rentalDTO::getIsPaid, existing::setIsPaid);

        RentalDTO updatedDTO = rentalMapper.toDTO(existing);
        validateNewRental(updatedDTO);

        Rental updated = rentalRepository.save(existing);
        logger.info("Rental with ID {} updated successfully", id);
        return rentalMapper.toDTO(updated);
    }

    /**
     * Delete a rental by ID.
     */
    public void deleteRental(Long id) {
        logger.info("Deleting rental with ID: {}", id);

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Rental with ID {} not found, cannot delete", id);
                    return new RentalNotFoundException(id);
                });

        rentalRepository.deleteById(rental.getId());
        logger.info("Rental with ID {} deleted successfully", id);
    }

    /**
     * Fetch rentals by user ID.
     */
    public List<RentalDTO> getRentalsByUser(Long userId) {
        logger.info("Fetching rentals by user ID: {}", userId);
        List<RentalDTO> rentals = rentalRepository.findByUserId(userId)
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Found {} rentals for user ID: {}", rentals.size(), userId);
        return rentals;
    }

    /**
     * Fetch rentals by car ID.
     */
    public List<RentalDTO> getRentalsByCar(Long carId) {
        logger.info("Fetching rentals by car ID: {}", carId);
        List<RentalDTO> rentals = rentalRepository.findByCarId(carId)
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Found {} rentals for car ID: {}", rentals.size(), carId);
        return rentals;
    }

    private void validateNewRental(RentalDTO rentalDTO) {
        if (rentalDTO.getStartDate() == null || rentalDTO.getEndDate() == null) {
            logger.error("StartDate or EndDate is null");
            throw new RentalCreationException("Rental dates cannot be null");
        }
        if (rentalDTO.getStartDate().isAfter(rentalDTO.getEndDate())) {
            logger.error("StartDate {} is after EndDate {}",
                    rentalDTO.getStartDate(), rentalDTO.getEndDate());
            throw new RentalCreationException("Invalid date range: start date is after end date.");
        }

        Long carId = rentalDTO.getCar().getId();
        List<Rental> overlappingCarRentals = rentalRepository.findOverlappingRentalsForCar(
                carId,
                rentalDTO.getStartDate(),
                rentalDTO.getEndDate()
        );

        if (rentalDTO.getId() != null) {
            overlappingCarRentals.removeIf(r -> r.getId().equals(rentalDTO.getId()));
        }

        if (!overlappingCarRentals.isEmpty()) {
            logger.error("Car with ID {} is not available between {} and {}",
                    carId, rentalDTO.getStartDate(), rentalDTO.getEndDate());
            throw new RentalCreationException("Car is not available for the selected dates.");
        }

        logger.info("Validation passed for rental: car={}, {} -> {}",
                carId, rentalDTO.getStartDate(), rentalDTO.getEndDate());
    }
}