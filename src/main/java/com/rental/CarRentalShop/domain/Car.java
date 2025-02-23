package com.rental.CarRentalShop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carid")
    private Long id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "registrationnumber", unique = true)
    private String registrationNumber;

    @Column(name = "rentalprice", precision = 10, scale = 2)
    private BigDecimal rentalPrice;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Rental> rentals;
}
