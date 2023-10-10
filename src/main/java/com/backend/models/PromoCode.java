package com.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "discount_percent")
    private double discount_percent;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
