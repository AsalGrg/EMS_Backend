package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "event_second_page_details")

public class EventSecondPageDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "event_cover_image_url")
    private String eventCoverPage;

    @Column(name = "cover_img_name")
    private String coverImgName;

    @Column(name = "hasStarring")
    private boolean hasStarring;


    @Column(name = "about_event")
    private String aboutEvent;


}
