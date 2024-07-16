package com.backend.services;

import com.backend.dtos.*;
import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterResponse;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.dtos.register.VerifyOtpRequest;
import com.backend.dtos.user.UserLoggedInSnippetResponse;
import com.backend.dtos.user.UserProfileDetailsResponse;
import com.backend.models.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    User getUserByUserId(int userId);

    void followUser(int vendorId);
    void unFollowUser(int vendorId);
    List<VendorResponseDto> getAllFollowing();
    RegisterResponse registerUser(RegisterUserDto registerUserDto);

    LoginRegisterResponse verifyOtp(VerifyOtpRequest verifyOtpRequest);

    LoginRegisterResponse verifyForgotPasswordOtp(VerifyOtpRequest verifyOtpRequest);
    LoginRegisterResponse changeForgotPassword(ChangeForgotPasswordDto changeForgotPasswordDto);

    LoginRegisterResponse loginUser(LoginUserDto loginUserDto);

    RegisterResponse forgotPassword(ForgotPasswordDto forgotPasswordDto);

    User getUserByUsername(String username);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    User saveUser(User user);

    UserLoggedInSnippetResponse getUserDetails();

    UserProfileDetailsResponse getUserProfile();

    UserProfileDetailsResponse getUserProfileByUserId(int userId, HttpServletRequest request);

    EditProfileDetails getEditProfileDetails();

    void editProfile(EditProfileDetails editProfileDetails);

}
