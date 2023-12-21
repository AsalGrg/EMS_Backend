package com.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "event_title")
    private String name;

    @OneToOne
    @JoinColumn(name= "event_location_id")
    private EventLocation eventLocation;

    @OneToOne
    @JoinColumn(name = "event_date_id")
    private EventDate eventDate;

    @Column(name = "event_cover_image_url")
    private String eventCoverPage;

    @Column(name = "hasStarring")
    private boolean hasStarring;

    @Column(name = "about_event")
    private String aboutEvent;

    @Column(name="isPrivate")
    private boolean isPrivate;

    @OneToOne
    @JoinColumn(name = "event_ticket_id")
    private EventTicket eventTicket;

    @OneToOne
    @JoinColumn(name = "visibility_id")
    private EventVisibility eventVisibility;

    @OneToOne
    @JoinColumn(name = "event_organizer_id")
    private User eventOrganizer;

    @OneToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;


}

