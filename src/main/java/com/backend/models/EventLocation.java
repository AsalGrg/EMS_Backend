package com.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "event_location")
public class EventLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "location_name")
    private String locationName;

    @OneToOne
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;
}
