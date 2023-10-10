package com.backend.models;


import jakarta.persistence.*;

@Entity
@Table(name= "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private  String description;

}
