package com.backend.controllers;


import com.backend.dtos.LoginRegisterResponse;
import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterResponse;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.dtos.register.VerifyOtpRequest;
import com.backend.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@Validated
//@RequestMapping("user/auth")
public class AuthController {


    //for now creating a session to store the user details

    @Qualifier("userservice")
    private final UserService userService;

    private final HttpSession httpSession;

    @Autowired
    public AuthController(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession= httpSession;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart("userDetails") @Valid RegisterUserDto registerUser)
                                          //thinking of giving a default image, if not included.
//                                          @RequestPart("userDp") @IsImage MultipartFile userDp)
    {
        RegisterResponse response = userService.registerUser(registerUser);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest){
        LoginRegisterResponse response= userService.verifyOtp(verifyOtpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserDto loginUser, HttpSession httpSession){
        LoginRegisterResponse response= userService.loginUser(loginUser);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/loggedInSnippet")
    public ResponseEntity<?> getUserLoggedInState(){
        return new ResponseEntity<>(userService.getUserDetails(), HttpStatus.OK);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserDetails(){
        userService.getUserDetails();

        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }



    //just for testing purposes only, to be removed in the future

}
