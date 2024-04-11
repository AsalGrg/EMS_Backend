package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "user_socials")
public class UserSocials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "linkedIn_link")
    private String linkedInLink;

    @Column(name = "twitter_link")
    private String twitterLink;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
