package com.backend.controllers;


import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.models.User;
import com.backend.repositories.UserRepository;
import com.backend.serviceImpls.UserServiceImplementation;
import com.backend.utils.FileNotEmpty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@RequestMapping("user/auth")
public class UserController {


    //for now creating a session to store the user details

    private UserServiceImplementation userService;

    private HttpSession httpSession;

    @Autowired
    public UserController(UserServiceImplementation userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession= httpSession;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart("userDetails") @Valid RegisterUserDto registerUser,
                                          @RequestPart(value = "userDp" , required = false) @Valid @FileNotEmpty(value = MediaType.IMAGE_JPEG_VALUE, message = "Please provide a JPEG image file for user dp.") MultipartFile userDp) {

        registerUser.setUserDp(userDp);

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
