package com.backend.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table
public class VendorFollowers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "followedBy")
    private User followedBy;

    @ManyToOne
    @JoinColumn(name = "followedTo")
    private User followedTo;

    @Column(name = "followed_date")
    private LocalDate followedDate;

}
