package com.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "ticket_payment")
public class TicketPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "total_amount")
    private  double total_amount;

    @Column(name = "promocodeUsed")
    private boolean promocodeUsed;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "promocode_id")
    private PromoCode promoCode;
}
