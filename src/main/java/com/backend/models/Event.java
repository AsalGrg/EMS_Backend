package com.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "published_date")
    private LocalDate published_date;


    @Column(name = "event_date")
    private LocalDate event_date;

    @Column(name = "description")
    private String description;

    @Column(name = "entry_fee")
    private double entryFee;

    @Column(name = "isPrivate")
    private boolean isPrivate;

    @Column(name = "seats")
    private int seats;

    @ManyToOne
    @JoinColumn(name = "event_hoster_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    @JsonIgnore
    private EventType eventType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_group" , joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

}

