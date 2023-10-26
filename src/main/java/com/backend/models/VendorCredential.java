package com.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "vender_credential")
public class VendorCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id")
    private Integer credential_id;

//    private String verification_photos_link
//    private String website_link
//    private Streing faceBook link
//    private String business_phoneNumber

    @Column(name = "valid_tax_clearance_certificate")
    private  String taxClearanceCertificate;

    @Column(name = "isVerified")
    private boolean isVerified;

    @Column(name = "isDeclined")
    private boolean isDeclined;

    @Column(name = "isTerminated")
    private boolean isTerminated;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "ratings")
    private double rating;
}
