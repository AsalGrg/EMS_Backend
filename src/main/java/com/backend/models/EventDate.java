package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "event_date")
public class EventDate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "event_publish_date")
    private LocalDateTime eventPublishDate;

    @Column(name = "event_start_date")
    private LocalDate eventStartDate;

    @Column(name = "event_start_time")
    private LocalTime eventStartTime;

    @Column(name = "event_end_date")
    private LocalDate eventEndDate;

    @Column(name = "event_end_time")
    private LocalTime eventEndTime;

    @Column(name = "display_start_time")
    private boolean displayStartTime;

    @Column(name = "display_end_time")
    private boolean displayEndTime;
}
