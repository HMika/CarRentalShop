package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.dto.RentalDTO;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.mapper.RentalMapper;
import com.rental.CarRentalShop.mapper.UserMapper;
import com.rental.CarRentalShop.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {
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

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RentalDTO getRentalById(Long id) {
        // Convert the Optional to a Stream, map to DTO, then pick the first element
        return rentalRepository.findById(id)
                .stream()
                .map(rentalMapper::toDTO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
    }

    public RentalDTO createRental(RentalDTO rentalDTO) {
        return java.util.stream.Stream.of(rentalDTO)
                .map(rentalMapper::toEntity)
                .map(rentalRepository::save)
                .map(rentalMapper::toDTO)
                .findFirst()
                // If somehow this stream was empty (shouldn't happen), we throw an exception
                .orElseThrow(() -> new IllegalArgumentException("Failed to create rental"));
    }

    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        return rentalRepository.findById(id)
                .stream()
                .map(existingRental -> {
                    existingRental.setStartDate(rentalDTO.getStartDate());
                    existingRental.setEndDate(rentalDTO.getEndDate());
                    existingRental.setIsPaid(rentalDTO.getIsPaid());

                    if (rentalDTO.getUser() != null) {
                        existingRental.setUser(userMapper.toEntity(rentalDTO.getUser()));
                    }
                    if (rentalDTO.getCar() != null) {
                        existingRental.setCar(carMapper.toEntity(rentalDTO.getCar()));
                    }

                    return existingRental;
                })
                // Save the transformed entity
                .map(rentalRepository::save)
                // Convert to DTO
                .map(rentalMapper::toDTO)
                // Get the first item or throw
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
    }

    public void deleteRental(Long id) {
        rentalRepository.findById(id)
                .stream()
                .findFirst()
                .ifPresentOrElse(
                        rental -> rentalRepository.deleteById(id),
                        () -> {
                            throw new IllegalArgumentException("Rental not found with ID: " + id);
                        }
                );
    }

    public List<RentalDTO> getRentalsByUser(Long userId) {
        return rentalRepository.findByUserId(userId)
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RentalDTO> getRentalsByCar(Long carId) {
        return rentalRepository.findByCarId(carId)
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
    }
}