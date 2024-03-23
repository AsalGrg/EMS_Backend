package com.backend.models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "event_first_page_details")
public class EventFirstPageDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "event_title")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "event_cat_id" )
    private EventCategory eventCategory;

    @OneToOne
    @JoinColumn(name= "event_location_id")
    private EventLocation eventLocation;

    @OneToOne
    @JoinColumn(name = "event_date_id")
    private EventDate eventDate;

}
