package com.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "event_third_page_details")
public class EventThirdPageDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "event_ticket_id")
    private EventTicket eventTicket;

}
