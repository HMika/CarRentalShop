package com.rental.CarRentalShop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "CarID")
    private Long id;

    @Column(name = "Make")
    private String make;

    @Column(name = "Model")
    private String model;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "RegistrationNumber", unique = true)
    private String registrationNumber;

    @Column(name = "RentalPrice")
    private Double rentalPrice;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Rental> rentals;
}
