package com.backend.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EditProfileDetails {

    private Object profileDp;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String userBio;

    private String instagramLink;
    private String facebookLink;
    private String linkedInLink;
    private String twitterLink;

}
