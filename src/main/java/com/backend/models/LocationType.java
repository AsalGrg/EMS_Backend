package com.backend.models;


import jakarta.persistence.*;

@Entity
@Table(name = "location_type")
public class LocationType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "location_type_title")
    private String locationTypeTitle;

    @Column(name = "description")
    private String description;
}
