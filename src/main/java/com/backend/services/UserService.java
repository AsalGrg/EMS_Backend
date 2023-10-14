package com.backend.services;

import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.models.User;

public interface UserService {

    public User registerUser(RegisterUserDto registerUserDto);

    public User loginUser(LoginUserDto loginUserDto);

}
