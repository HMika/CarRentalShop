package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Rental;
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
    public RentalService(RentalRepository rentalRepository, RentalMapper rentalMapper,
                         UserMapper userMapper, CarMapper carMapper) {
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
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
        return rentalMapper.toDTO(rental);
    }

    public RentalDTO createRental(RentalDTO rentalDTO) {
        Rental rental = rentalMapper.toEntity(rentalDTO);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toDTO(savedRental);
    }

    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        return rentalRepository.findById(id)
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

                    Rental updatedRental = rentalRepository.save(existingRental);
                    return rentalMapper.toDTO(updatedRental);
                })
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
    }

    public void deleteRental(Long id) {
        if (!rentalRepository.existsById(id)) {
            throw new IllegalArgumentException("Rental not found with ID: " + id);
        }
        rentalRepository.deleteById(id);
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
