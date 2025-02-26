package com.rental.CarRentalShop.repository;

import com.rental.CarRentalShop.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(Long userId);

    List<Rental> findByCarId(Long carId);

    // Finds any rentals for a given car that intersect [startDate, endDate]
    @Query("""
                SELECT r 
                  FROM Rental r 
                 WHERE r.car.id = :carId
                   AND r.startDate <= :endDate
                   AND r.endDate   >= :startDate
            """)
    List<Rental> findOverlappingRentalsForCar(@Param("carId") Long carId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    // Ensuring user can't double-book any car:
    @Query("""
                SELECT r 
                  FROM Rental r 
                 WHERE r.user.id = :userId
                   AND r.startDate <= :endDate
                   AND r.endDate   >= :startDate
            """)
    List<Rental> findOverlappingRentalsForUser(@Param("userId") Long userId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}
