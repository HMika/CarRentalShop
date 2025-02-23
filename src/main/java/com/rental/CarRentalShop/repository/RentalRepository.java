package com.rental.CarRentalShop.repository;

import com.rental.CarRentalShop.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(Long userId);

    List<Rental> findByCarId(Long carId);
}
