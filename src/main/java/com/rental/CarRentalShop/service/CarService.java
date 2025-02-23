package com.rental.CarRentalShop.service;

import com.rental.CarRentalShop.domain.Car;
import com.rental.CarRentalShop.dto.CarDTO;
import com.rental.CarRentalShop.mapper.CarMapper;
import com.rental.CarRentalShop.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Autowired
    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(carMapper::toDTO).orElse(null);
    }

    public CarDTO createCar(CarDTO carDTO) {
        Car car = carMapper.toEntity(carDTO);
        Car savedCar = carRepository.save(car);
        return carMapper.toDTO(savedCar);
    }

    public CarDTO updateCar(Long id, CarDTO carDTO) {
        if (!carRepository.existsById(id)) {
            return null;
        }
        Car car = carMapper.toEntity(carDTO);
        car.setId(id);
        Car updatedCar = carRepository.save(car);
        return carMapper.toDTO(updatedCar);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
