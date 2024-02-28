package com.backend.serviceImpls;

import com.backend.configs.JwtUtils;
import com.backend.dtos.LoginRegisterResponse;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.dtos.internals.EmailVerificationServiceResponse;
import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterResponse;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.dtos.register.VerifyOtpRequest;
import com.backend.dtos.user.UserLoggedInSnippetResponse;
import com.backend.dtos.user.UserProfileDetailsResponse;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.EventRepository;
import com.backend.repositories.RoleRepository;
import com.backend.repositories.UserRepository;
import com.backend.services.*;
import com.backend.utils.EmailMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private EmailService emailService;
    private final EventPhysicalLocationDetailsService eventPhysicalLocationDetailsService;
    private final RoleRepository roleRepository;
    private EmailVerificationService emailVerificationService;
    private EventRepository eventRepository;
    private JwtUtils jwtUtils;
    private final CloudinaryUploadService cloudinary;

    private EmailMessages emailMessages;

    @Autowired
    public UserServiceImplementation
            (UserRepository userRepository,
             EmailService emailService,
             RoleRepository roleRepository,
             CloudinaryUploadService cloudinary,
             EmailVerificationService emailVerificationService,
             EmailMessages emailMessages,
             JwtUtils jwtUtils,
             EventPhysicalLocationDetailsService eventPhysicalLocationDetailsService,
             EventRepository eventRepository){

        this.userRepository= userRepository;
        this.emailService=emailService;
        this.roleRepository= roleRepository;
        this.cloudinary= cloudinary;
        this.emailVerificationService= emailVerificationService;
        this.emailMessages= emailMessages;
        this.jwtUtils= jwtUtils;
        this.eventRepository= eventRepository;
        this.eventPhysicalLocationDetailsService= eventPhysicalLocationDetailsService;
    }

    public LoginRegisterResponse registerAdmin(RegisterUserDto registerUserDto){

        User user= new User();
        user.setEmail(registerUserDto.getEmail());
        user.setUsername(registerUserDto.getUsername());
        user.setAddress(registerUserDto.getAddress());

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

        userRepository.save(user);

        return LoginRegisterResponse
                .builder().
                message("User register successful")
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public EventResponseDto changeToEventDto(Event event, EventPhysicalLocationDetails physicalLocationDetails){
        if(physicalLocationDetails==null) {
            return EventResponseDto.builder().
                    eventName(event.getName())
                    .eventCoverImgUrl(event.getEventCoverPage())
                    .startDate(event.getEventDate().getEventStartDate())
                    .endDate(event.getEventDate().getEventEndDate())
                    .category(event.getEventCategory().getTitle())
                    .ticketType(event.getEventTicket().getTicketType().getTitle())
                    .ticketPrice(event.getEventTicket().getTicketPrice())
                    .build();
        }
        return EventResponseDto.builder().
                eventName(event.getName())
                .eventCoverImgUrl(event.getEventCoverPage())
                .startDate(event.getEventDate().getEventStartDate())
                .endDate(event.getEventDate().getEventEndDate())
                .category(event.getEventCategory().getTitle())
                .ticketType(event.getEventTicket().getTicketType().getTitle())
                .ticketPrice(event.getEventTicket().getTicketPrice())
                .country(physicalLocationDetails.getCountry())
                .location_display_name(physicalLocationDetails.getDisplayName())
                .lat(physicalLocationDetails.getLat())
                .lon(physicalLocationDetails.getLon())
                .build();
    }



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

       User user = userRepository.findByEmailAndPassword(loginUserDto.getEmail(), loginUserDto.getPassword())
                .orElseThrow(()-> new ResourceNotFoundException("User with the given credentials does not exist!"));

       //takes username for user details
       UserDetails userDetails = loadUserByUsername(user.getUsername());

       String token = jwtUtils.generateToken(userDetails);

       return LoginRegisterResponse.
               builder()
               .message("User login successful")
               .jwtToken(token)
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

    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public UserLoggedInSnippetResponse getUserDetails() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user= getUserByUsernameOrEmail(username);
        return UserLoggedInSnippetResponse.builder()
                .userDp(user.getUserDp())
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserProfileDetailsResponse getUserProfile() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByUsername(username);
        List<Event> userEvents = eventRepository.getEventsByUser(user);

        UserProfileDetailsResponse.UserSnippetDetails userSnippetDetails= UserProfileDetailsResponse.UserSnippetDetails.builder().
                username(user.getUsername()).
                userDp(user.getUserDp())
                .userIntro("This is intro")
                .build();


        return UserProfileDetailsResponse.builder()
                .userSnippetDetails(userSnippetDetails)
                .noOfEvents(userEvents.size())
                .pastEvents(
                        userEvents.stream()
                                .filter(each-> each.getEventDate().getEventEndDate().isBefore(LocalDate.now()))
                                .map(each-> changeToEventDto(each, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(each.getEventLocation())))
                                .toList()
                ).
                upcomingEvents(
                userEvents.stream()
                        .filter(each-> each.getEventDate().getEventEndDate().isAfter(LocalDate.now()))
                        .map(each-> changeToEventDto(each, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(each.getEventLocation())))
                        .toList())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid user"));
    }
}
