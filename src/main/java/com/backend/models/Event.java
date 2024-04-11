package com.backend.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(name="isPrivate")
    private boolean isPrivate;

    @Column(name = "event_status")
    private String eventStatus;

    @Column(name = "page_status")
    private int pageStatus;

    @OneToOne
    @JoinColumn(name = "visibility_id")
    private EventVisibility eventVisibility;

    @ManyToOne
    @JoinColumn(name = "event_organizer_id")
    private User eventOrganizer;

    @OneToOne
    @JoinColumn(name = "event_first_page_details")
    private EventFirstPageDetails eventFirstPageDetails;

    @OneToOne
    @JoinColumn(name = "event_second_page_details")
    private EventSecondPageDetails eventSecondPageDetails;

    @OneToOne
    @JoinColumn(name = "event_third_page_details")
    private EventThirdPageDetails eventThirdPageDetails;

    @Column(name = "event_added_date")
    private LocalDate eventAddedDate;

}

