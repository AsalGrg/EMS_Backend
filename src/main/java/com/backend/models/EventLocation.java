package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "event_location")
public class EventLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "location_name")
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;
}
