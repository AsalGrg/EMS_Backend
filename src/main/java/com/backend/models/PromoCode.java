package com.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "discount")
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "use_limit")
    //can be unlimited, first 100
    private String limit;

    @Column(name = "available_quantity")
    //can be unlimited, first 100
    private String availableQuantity;

    @Column(name = "applicable_on")
    //can be unlimited, first 100
    private String applicableOn;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "hasNoEnd")
    private boolean hasNoEnd;

    @ManyToOne
    @JoinColumn(name = "promo_code_type_id")
    private PromoCodeType promoCodeType;
}
