package com.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "user")
@Slf4j
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer userId;

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

    @Column(name = "user_bio")
    private String userBio;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_verified")
    private boolean isVerified;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles" , joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role>  userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = userRoles
                .stream()
                .peek(role -> log.info("Role: "+ role.getTitle()))
                .map(permission -> new SimpleGrantedAuthority(permission.getTitle()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
