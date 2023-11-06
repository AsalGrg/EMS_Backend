package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "net_total")
    private  double net_total;

    @Column(name = "grand_total")
    private double grand_total;

    @Column(name = "quantity")
    private int quantity;

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
