package com.rental.carrentalshop.service;

import com.rental.carrentalshop.domain.Car;
import com.rental.carrentalshop.dto.CarDTO;
import com.rental.carrentalshop.exception.car.CarDeletionException;
import com.rental.carrentalshop.exception.car.CarNotFoundException;
import com.rental.carrentalshop.exception.car.DuplicateCarException;
import com.rental.carrentalshop.exception.car.InvalidCarDataException;
import com.rental.carrentalshop.mapper.CarMapper;
import com.rental.carrentalshop.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public List<CarDTO> getAllCars() {
        logger.info("Fetching all cars from the database.");
        return carRepository.findAll()
                .stream()
                .map(carMapper::toDTO)
                .toList();
    }

    public CarDTO getCarById(Long id) {
        logger.info("Fetching car with ID: {}", id);
        return carRepository.findById(id)
                .map(carMapper::toDTO)
                .orElseThrow(() -> new CarNotFoundException(id));
    }

    public CarDTO createCar(CarDTO carDTO) {
        logger.info("Creating a new car: {}", carDTO);

        if (carDTO.getRegistrationNumber() == null || carDTO.getRegistrationNumber().isBlank()) {
            throw new InvalidCarDataException("Car registration number cannot be empty.");
        }

        try {
            Car car = carMapper.toEntity(carDTO);
            Car savedCar = carRepository.save(car);
            logger.debug("Car successfully created with ID: {}", savedCar.getId());
            return carMapper.toDTO(savedCar);
        } catch (DataIntegrityViolationException e) {
            logger.error("Duplicate registration number: {}", carDTO.getRegistrationNumber());
            throw new DuplicateCarException("Car with registration number " + carDTO.getRegistrationNumber() + " already exists.");
        }
    }

    @Transactional
    public CarDTO updateCar(Long id, CarDTO carDTO) {
        logger.info("Updating car with ID: {}", id);

        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        existingCar.setMake(carDTO.getMake());
        existingCar.setModel(carDTO.getModel());
        existingCar.setYear(carDTO.getYear());
        existingCar.setRegistrationNumber(carDTO.getRegistrationNumber());
        existingCar.setRentalPrice(carDTO.getRentalPrice());

        Car updatedCar = carRepository.save(existingCar);
        logger.debug("Car with ID {} successfully updated.", id);
        return carMapper.toDTO(updatedCar);
    }

    @Transactional
    public void deleteCar(Long id) {
        logger.info("Attempting to delete car with ID: {}", id);

        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException(id);
        }

        try {
            carRepository.deleteById(id);
            logger.info("Car with ID {} successfully deleted.", id);
        } catch (DataAccessException e) {
            logger.error("Error deleting car with ID: {}", id, e);
            throw new CarDeletionException(id);
        }
    }
}
