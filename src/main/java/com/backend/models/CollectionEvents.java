package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Collection_events")
public class CollectionEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private EventCollection collection;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
