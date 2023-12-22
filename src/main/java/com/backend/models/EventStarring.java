package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "event_starrings")
public class EventStarring {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "starring1_photo")
    private String starring1Photo;

    @Column(name = "starring1_name")
    private String starring1Name;

    @Column(name = "starring2_photo")
    private String starring2Photo;

    @Column(name = "starring2_name")
    private String starring2Name;

    @Column(name = "starring3_photo")
    private String starring3Photo;

    @Column(name = "starring3_name")
    private String starring3Name;

    @Column(name = "starring4_photo")
    private String starring4Photo;

    @Column(name = "starring4_name")
    private String starring4Name;

    @Column(name = "starring5_photo")
    private String starring5Photo;

    @Column(name = "starring5_name")
    private String starring5Name;

}
