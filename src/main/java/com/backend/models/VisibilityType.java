package com.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.net.Inet4Address;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "visibility_type")
public class VisibilityType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "visibility_type_name")
    private String title;

    @Column(name = "description")
    private String description;

}
