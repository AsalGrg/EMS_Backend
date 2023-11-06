package com.backend.services;

import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.models.User;

public interface UserService {

    User registerUser(RegisterUserDto registerUserDto);

    User loginUser(LoginUserDto loginUserDto);

    User getUserByUsername(String username);

    User getUserByUsernameOrEmail(String usernameOrEmail);





}
