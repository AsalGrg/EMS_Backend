package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ticket_payment")
public class TicketPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "grand_total")
    private double grand_total;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "promoCodeUsed")
    private boolean promoCodeUsed;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne()
    @JoinColumn(name = "promocode_id")
    private PromoCode promoCode;
}
