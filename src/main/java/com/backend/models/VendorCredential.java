package com.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @Column(name = "business_registration_document")
    private String vendorRegistrationDocument;

    @Column(name = "valid_tax_clearance_certificate")
    private  String taxClearanceCertificate;

    @Column(name = "vendor_registration_filled_form")
    private  String vendorRegistrationFilledForm;

    @Column(name = "isVerified")
    private boolean isVerified;

    @Column(name = "isDeclined")
    private boolean isDeclined;

    @Column(name = "isTerminated")
    private boolean isTerminated;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "tiktok_link")
    private String tiktokLink;

    @Column(name = "linkedIn_link")
    private String linkedinLink;

    @Column(name = "vendor_description")
    private String vendorDescription;

    @Column(name = "business_email")
    private String businessEmail;

    @Column(name = "contact_number1")
    private String contactNumber1;

    @Column(name = "contact_number2")
    private String contactNumber2;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "ratings")
    private double rating;
}
