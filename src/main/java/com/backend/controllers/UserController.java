package com.backend.controllers;


import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.models.User;
import com.backend.repositories.UserRepository;
import com.backend.serviceImpls.UserServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("user/auth")
public class UserController {


    //for now creating a session to store the user details

    private UserServiceImplementation userService;

    @Autowired
    public UserController(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDto registerUser, HttpSession httpSession){
        User user= this.userService.registerUser(registerUser);

        if(user!=null){
            httpSession.setAttribute("CurrentUser", user.getUsername());
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserDto loginUser, HttpSession httpSession){
        User user= this.userService.loginUser(loginUser);

        if(user!=null){
            httpSession.setAttribute("CurrentUser", user.getUsername());
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    //just for testing purposes only, to be removed in the future
    @PostMapping("/admin-signup")
    public ResponseEntity<?> signUpAdmin(@Valid @RequestBody RegisterUserDto registerUserDto, HttpSession httpSession){

        User user= this.userService.registerAdmin(registerUserDto);

        if(user!=null){
            httpSession.setAttribute("CurrentUser", user.getUsername());
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}
