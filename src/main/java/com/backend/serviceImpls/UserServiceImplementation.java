package com.backend.serviceImpls;

import com.backend.dtos.LoginRegisterResponse;
import com.backend.dtos.internals.EmailVerificationServiceResponse;
import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterResponse;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.dtos.register.VerifyOtpRequest;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.EmailVerification;
import com.backend.models.Role;
import com.backend.models.User;
import com.backend.repositories.RoleRepository;
import com.backend.repositories.UserRepository;
import com.backend.services.EmailService;
import com.backend.services.EmailVerificationService;
import com.backend.services.UserService;
import com.backend.utils.EmailMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private EmailVerificationService emailVerificationService;
    private EmailService emailService;
    private final CloudinaryUploadServiceImplementation cloudinary;

    private EmailMessages emailMessages;

    @Autowired
    public UserServiceImplementation
            (UserRepository userRepository, RoleRepository roleRepository,
             CloudinaryUploadServiceImplementation cloudinary,
             EmailVerificationService emailVerificationService,
             EmailMessages emailMessages,
             EmailService emailService){

        this.userRepository= userRepository;
        this.roleRepository= roleRepository;
        this.cloudinary= cloudinary;
        this.emailVerificationService= emailVerificationService;
        this.emailMessages= emailMessages;
        this.emailService= emailService;
    }


    //here contains the overridden method from the UserDetailsService

    public LoginRegisterResponse registerAdmin(RegisterUserDto registerUserDto){

        User user= new User();
        user.setEmail(registerUserDto.getEmail());
        user.setUsername(registerUserDto.getUsername());
        user.setAddress(registerUserDto.getAddress());
        user.setEvents(null);

        roleRepository.findByTitle("ADMIN").ifPresentOrElse(role -> user.setUserRoles(List.of(role)) , ()->{
            Role role = new Role();
            role.setTitle("ADMIN");
            role.setDescription("System Admin");

            Role savedRole= roleRepository.save(role);
            user.setUserRoles(List.of(savedRole));
        });
        //encoding the password here and then storing it, to be continued
        user.setPassword(registerUserDto.getPassword());

        //no events are there in the user at first
        user.setEvents(null);

        userRepository.save(user);

        return LoginRegisterResponse
                .builder().
                message("User register successful")
                .timeStamp(LocalDateTime.now())
                .build();
    }




    //overridden method from the UserService interface
    @Override
    public RegisterResponse registerUser(RegisterUserDto registerUserDto) {

        boolean usernameExists= userRepository.existsByUsername(registerUserDto.getUsername());
        boolean emailExists= userRepository.existsByEmail(registerUserDto.getEmail());

        if(usernameExists){
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        if(emailExists){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        String userDpUrl= null;

        if (registerUserDto.getUserDp()!=null){
            userDpUrl= cloudinary.uploadImage(registerUserDto.getUserDp(), "User Dps");
        }

        User user= new User();
        user.setEmail(registerUserDto.getEmail());
        user.setUsername(registerUserDto.getUsername());
        user.setAddress(registerUserDto.getAddress());
        user.setEvents(null);
        user.setPhoneNumber(registerUserDto.getPhoneNumber());

        //saving the url of the user dp in the
        user.setUserDp(userDpUrl);


        roleRepository.findByTitle("USER").ifPresentOrElse(role -> user.setUserRoles(List.of(role)) , ()->{
            Role role = new Role();
            role.setTitle("USER");
            role.setDescription("Normal User");

            Role savedRole= roleRepository.save(role);
            user.setUserRoles(List.of(savedRole));
    });
        //encoding the password here and then storing it, to be continued
        user.setPassword(registerUserDto.getPassword());

        //no events are there in the user at first
        user.setEvents(null);
        user.setEnabled(false);
        user.setVerified(false);
        saveUser(user);


        EmailVerificationServiceResponse emailVerificationServiceResponse= emailVerificationService.getEmailVerification(user);

        Map<String, String> emailVerificationMessages= emailMessages.userEmailVerificationOtpMessage(user.getEmail(), emailVerificationServiceResponse.getOtp());

        String subject = emailVerificationMessages.get("subject");
        String body = emailVerificationMessages.get("message");

        emailService.sendEmail(
                "asal.gurung.a21.2@icp.edu.np",
                subject,
                body
        );
        return RegisterResponse
                .builder().
                message("Verify OTP")
                .verificationToken(emailVerificationServiceResponse.getVerificationToken())
                .timestamp(LocalDateTime.now())
                .userEmail(user.getEmail())
                .build();
    }

    public LoginRegisterResponse verifyOtp(VerifyOtpRequest verifyOtpRequest){
        EmailVerification emailVerification= emailVerificationService.validateOtp(verifyOtpRequest.getVerificationToken()
                , verifyOtpRequest.getOtp());
        User user= emailVerification.getUser();
        user.setVerified(true);
        user.setEnabled(true);

        userRepository.save(user);

        return LoginRegisterResponse
                .builder()
                .message("Email Verification Successful")
                .timeStamp(LocalDateTime.now())
                .build();
    }


    @Override
    public LoginRegisterResponse loginUser(LoginUserDto loginUserDto){

       userRepository.findByEmailAndPassword(loginUserDto.getEmail(), loginUserDto.getPassword())
                .orElseThrow(()-> new ResourceNotFoundException("User with the given credentials does not exist!"));
       return LoginRegisterResponse.
               builder()
               .message("User login successful")
               .timeStamp(LocalDateTime.now())
               .build();

    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(username + "not found"));
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail){
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).
                orElseThrow(()-> new ResourceNotFoundException("Invalid username or email"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid user"));
    }
}
