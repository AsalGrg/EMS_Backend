package com.backend.models;


import jakarta.persistence.*;
import lombok.*;

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

}

