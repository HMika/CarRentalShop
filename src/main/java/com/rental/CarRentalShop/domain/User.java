package com.rental.CarRentalShop.domain;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // maps to SERIAL (auto-increment) in PostgreSQL

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
}
