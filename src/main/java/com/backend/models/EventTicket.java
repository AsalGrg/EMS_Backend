package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "event_ticket")
public class EventTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="ticket_type_id")
    private TicketType ticketType;

    @Column(name = "ticket_price")
    private double ticketPrice;

    @Column(name = "ticket_name")
    private String ticketName;


    @Column(name = "ticket_quantity")
    private int ticketQuantity;

    @Column(name = "ticket_start_date")
    private LocalDate ticketStartDate;

    @Column(name = "ticket_start_time")
    private LocalTime ticketStartTime;

    @Column(name = "ticket_end_date")
    private LocalDate ticketEndDate;

    @Column(name = "ticket_end_time")
    private LocalTime ticketEndTime;

    @Column(name = "ticket_sold")
    private int ticketSold;
}
