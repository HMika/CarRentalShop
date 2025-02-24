package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Autowired
    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public List<CarDTO> getAllCars() {
        logger.info("Fetching all cars from the database.");
        List<CarDTO> cars = carRepository.findAll()
                .stream()
                .map(carMapper::toDTO)
                .collect(Collectors.toList());
        logger.debug("Successfully retrieved {} cars.", cars.size());
        return cars;
    }

    public CarDTO getCarById(Long id) {
        logger.info("Fetching car with ID: {}", id);
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            logger.debug("Car found: {}", car.get());
            return carMapper.toDTO(car.get());
        } else {
            logger.warn("Car with ID {} not found.", id);
            return null;
        }
    }

    public CarDTO createCar(CarDTO carDTO) {
        logger.info("Creating a new car: {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        Car savedCar = carRepository.save(car);
        logger.debug("Car successfully created with ID: {}", savedCar.getId());
        return carMapper.toDTO(savedCar);
    }

    public CarDTO updateCar(Long id, CarDTO carDTO) {
        logger.info("Updating car with ID: {}", id);

        if (!carRepository.existsById(id)) {
            logger.warn("Car with ID {} does not exist. Update aborted.", id);
            return null;
        }

        Car car = carMapper.toEntity(carDTO);
        car.setId(id);
        Car updatedCar = carRepository.save(car);

        logger.debug("Car with ID {} successfully updated.", id);
        return carMapper.toDTO(updatedCar);
    }

    public void deleteCar(Long id) {
        logger.info("Attempting to delete car with ID: {}", id);

        if (!carRepository.existsById(id)) {
            logger.warn("Car with ID {} does not exist. Delete aborted.", id);
            return;
        }

        carRepository.deleteById(id);
        logger.info("Car with ID {} successfully deleted.", id);
    }
}
