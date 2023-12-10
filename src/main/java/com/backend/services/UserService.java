package com.backend.services;

import com.backend.dtos.LoginRegisterResponse;
import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterResponse;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.dtos.register.VerifyOtpRequest;
import com.backend.models.User;

public interface UserService {

    RegisterResponse registerUser(RegisterUserDto registerUserDto);

    LoginRegisterResponse verifyOtp(VerifyOtpRequest verifyOtpRequest);

    LoginRegisterResponse loginUser(LoginUserDto loginUserDto);

    User getUserByUsername(String username);

    User getUserByUsernameOrEmail(String usernameOrEmail);





}
