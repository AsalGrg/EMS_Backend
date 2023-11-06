package com.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Column(name = "name")
    @NotNull(message = "Promo code name is required")
    private String name;

    @Column(name = "discount_amount")
    @NotNull(message = "Provide promo code discount amount")
    private Double discount_amount;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
