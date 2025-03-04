package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.domain.Rental;
import com.rental.CarRentalShop.domain.User;
import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.exception.car.CarNotFoundException;
import com.rental.CarRentalShop.exception.rental.RentalCreationException;
import com.rental.CarRentalShop.exception.rental.RentalNotFoundException;
import com.rental.CarRentalShop.exception.user.UserNotFoundException;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.CarRepository;
import com.rental.CarRentalShop.repository.RentalRepository;
import com.rental.CarRentalShop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserMapper userMapper;
    private final CarMapper carMapper;
    private final CarRepository carRepository;

    private final UserRepository userRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         RentalMapper rentalMapper,
                         UserMapper userMapper,
                         CarMapper carMapper,
                         CarRepository carRepository,
                         UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.userMapper = userMapper;
        this.carMapper = carMapper;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
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
        rentals.forEach(rental -> rental.getUser().setPassword("*******"));
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
        rental.getUser().setPassword("*******");
        return rentalMapper.toDTO(rental);
    }

    /**
     * Create a new rental.
     */

    public RentalDTO createRental(RentalDTO rentalDTO) {
        logger.info("Creating a new rental");

        validateNewRental(rentalDTO, null);  // Pass null for new rental creation

        try {
            Car car = carRepository.findById(rentalDTO.getCar().getId())
                    .orElseThrow(() -> new CarNotFoundException(rentalDTO.getCar().getId()));
            rentalDTO.setCar(carMapper.toDTO(car));

            User user = userRepository.findById(rentalDTO.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(rentalDTO.getUser().getId()));
            rentalDTO.setUser(userMapper.toDTO(user));

            logger.info("Fetched Car: ID={}, Rental Price={}", car.getId(), car.getRentalPrice());
            logger.info("Fetched User: ID={}", user.getId());

            long rentalDays = ChronoUnit.DAYS.between(rentalDTO.getStartDate(), rentalDTO.getEndDate());
            if (rentalDays <= 0) {
                throw new RentalCreationException("Rental period must be at least 1 day.");
            }

            BigDecimal totalPrice = car.getRentalPrice().multiply(BigDecimal.valueOf(rentalDays));
            rentalDTO.setTotalPrice(totalPrice);

            Rental rentalEntity = rentalMapper.toEntity(rentalDTO);
            rentalEntity.setTotalPrice(totalPrice);

            Rental savedRental = rentalRepository.save(rentalEntity);
            logger.info("Successfully created rental with ID: {} and total price: {}", savedRental.getId(), totalPrice);
            savedRental.getUser().setPassword("*******");
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

        if (rentalDTO.getUser() != null && rentalDTO.getUser().getId() != null) {
            User user = userRepository.findById(rentalDTO.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(rentalDTO.getUser().getId()));
            existing.setUser(user);
            rentalDTO.setUser(userMapper.toDTO(user));
        }

        if (rentalDTO.getCar() != null && rentalDTO.getCar().getId() != null &&
                !rentalDTO.getCar().getId().equals(existing.getCar().getId())) {
            Car car = carRepository.findById(rentalDTO.getCar().getId())
                    .orElseThrow(() -> new CarNotFoundException(rentalDTO.getCar().getId()));
            existing.setCar(car);
            rentalDTO.setCar(carMapper.toDTO(car));
        }

        if (rentalDTO.getStartDate() != null) {
            existing.setStartDate(rentalDTO.getStartDate());
        }
        if (rentalDTO.getEndDate() != null) {
            existing.setEndDate(rentalDTO.getEndDate());
        }

        validateNewRental(rentalMapper.toDTO(existing), id);

        long rentalDays = ChronoUnit.DAYS.between(existing.getStartDate(), existing.getEndDate());
        if (rentalDays <= 0) {
            throw new RentalCreationException("Rental period must be at least 1 day.");
        }
        BigDecimal totalPrice = existing.getCar().getRentalPrice().multiply(BigDecimal.valueOf(rentalDays));
        existing.setTotalPrice(totalPrice);

        Rental updated = rentalRepository.save(existing);
        logger.info("Rental with ID {} updated successfully", id);
        updated.getUser().setPassword("*******");
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
        rentals.forEach(rental -> rental.getUser().setPassword("*******"));
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
        rentals.forEach(rental -> rental.getUser().setPassword("*******"));
        return rentals;
    }

    private void validateNewRental(RentalDTO rentalDTO, Long rentalId) {
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


        List<Rental> filteredRentals = overlappingCarRentals.stream()
                .filter(r -> rentalId == null || !r.getId().equals(rentalId))
                .toList();

        if (!filteredRentals.isEmpty()) {
            logger.error("Car with ID {} is not available between {} and {}", carId, rentalDTO.getStartDate(), rentalDTO.getEndDate());
            throw new RentalCreationException("Car is not available for the selected dates.");
        }

        logger.info("Validation passed for rental: car={}, {} -> {}", carId, rentalDTO.getStartDate(), rentalDTO.getEndDate());

    }
}