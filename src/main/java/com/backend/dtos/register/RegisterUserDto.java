package com.backend.dtos.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RegisterUserDto {

    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;


    @Email(message = "Enter a valid email")
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 12, min = 6, message = "Password must be of 6-12 characters")
    private String password;

    @NotEmpty(message = "Address cannot be empty")
    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Phone number is required")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    private MultipartFile userDp;
}
