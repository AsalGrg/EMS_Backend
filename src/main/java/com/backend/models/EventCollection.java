package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "EventCollection")

public class EventCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "collection_name")
    private String collectionName;

    @Column(name = "collection_description")
    private String collectionDescription;

    @Column(name = "collection_coverImage")
    private String coverImage;

    @Column(name = "collection_img_name")
    private String coverImgName;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "isShown")
    private boolean isShown;
}
