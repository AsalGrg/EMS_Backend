package com.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "venue")
    private String eventVenue;

    @Column(name = "published_date")
    private LocalDate published_date;


    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private Time eventTime;

    @Column(name = "description")
    private String description;

    @Column(name = "entry_fee")
    private double entryFee;

    @Column(name = "isPrivate")
    private boolean isPrivate;

    @Column(name = "available_seats")
    private int seats;

    @Column(name = "tickets_sold")
    private int ticketSold;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "isAccepted")
    private boolean isAccepted;

    @Column(name = "isDeclined")
    private boolean isDeclined;


    @ManyToOne
    @JoinColumn(name = "event_organizer_id")
    private User event_organizer;

    @ManyToOne
    @JoinColumn(name = "event_vendor_id")
    private User event_vendor;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    @JsonIgnore
    private EventType eventType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_group" , joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> event_group;

}

