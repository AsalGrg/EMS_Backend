package com.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Getter
@Setter

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer user_id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_dp")
    private String userDp;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @ManyToMany
    @JoinTable(name = "user_roles" , joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role>  userRoles;

    @ManyToMany(mappedBy = "event_group")
    @JsonIgnore
    private Set<Event> events;

}
