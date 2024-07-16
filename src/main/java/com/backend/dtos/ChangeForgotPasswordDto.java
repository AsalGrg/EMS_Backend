package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeForgotPasswordDto {

    private String newPassword;
    private String emailVerificationToken;
}
