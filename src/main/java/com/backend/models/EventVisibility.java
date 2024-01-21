package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "event-visibility")
public class EventVisibility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "event_access_password")
    private String eventAccessPassword;

    @ManyToOne
    @JoinColumn(name = "visibility_type_id")
    private VisibilityType visibilityType;
}
