package com.rental.carrentalshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rentals")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalid")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "carid", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Car car;

    @Column(name = "startdate", nullable = false)
    private LocalDate startDate;

    @Column(name = "enddate", nullable = false)
    private LocalDate endDate;

    @Column(name = "ispaid")
    private Boolean isPaid;

    @Column(name = "totalprice", nullable = false)
    private BigDecimal totalPrice;
}
