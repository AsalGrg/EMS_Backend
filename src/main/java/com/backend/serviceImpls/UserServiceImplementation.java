package com.backend.serviceImpls;

import com.backend.configs.JwtUtils;
import com.backend.dtos.EditProfileDetails;
import com.backend.dtos.LoginRegisterResponse;
import com.backend.dtos.VendorResponseDto;
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
import com.backend.repositories.UserSocailRepository;
import com.backend.services.*;
import com.backend.utils.EmailMessages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private final VendorFollowerService vendorFollowerService;
    private EmailMessages emailMessages;
    private UserSocailRepository userSocailRepository;

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
             EventRepository eventRepository,
             VendorFollowerService vendorFollowerService,
             UserSocailRepository userSocailRepository
             ){

        this.userRepository= userRepository;
        this.emailService=emailService;
        this.roleRepository= roleRepository;
        this.cloudinary= cloudinary;
        this.emailVerificationService= emailVerificationService;
        this.emailMessages= emailMessages;
        this.jwtUtils= jwtUtils;
        this.eventRepository= eventRepository;
        this.eventPhysicalLocationDetailsService= eventPhysicalLocationDetailsService;
        this.vendorFollowerService= vendorFollowerService;
        this.userSocailRepository= userSocailRepository;
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
//        return new EventResponseDto(event.getName(), event.getEventDate().;, event.getPublished_date(), event.getEntryFee(),event.getEventType().getTitle());
        EventFirstPageDetails eventFirstPageDetails = event.getEventFirstPageDetails();
        EventSecondPageDetails eventSecondPageDetails = event.getEventSecondPageDetails();
        EventThirdPageDetails eventThirdPageDetails = event.getEventThirdPageDetails();

        if (physicalLocationDetails == null) {
            return EventResponseDto.builder()
                    .eventId(event.getId())
                    .eventStatus(event.getEventStatus())
                    .pageStatus(event.getPageStatus())
                    .eventName(eventFirstPageDetails != null ? eventFirstPageDetails.getName() : null)
                    .eventCoverImgUrl(eventSecondPageDetails != null ? eventSecondPageDetails.getEventCoverPage() : null)
                    .startDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartDate() : null)
                    .endDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventEndDate() : null)
                    .startTime(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartTime() : null)
                    .category(eventFirstPageDetails != null && eventFirstPageDetails.getEventCategory() != null ? eventFirstPageDetails.getEventCategory().getTitle() : null)
                    .ticketType(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null && eventThirdPageDetails.getEventTicket().getTicketType() != null ? eventThirdPageDetails.getEventTicket().getTicketType().getTitle() : null)
                    .ticketPrice(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketPrice() : null)
                    .ticketsForSale(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketQuantity() : null)
                    .ticketsSold(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketSold() : null)
                    .organizerName(event.getEventOrganizer() != null ? event.getEventOrganizer().getUsername() : null)
                    .country("")
                    .location_display_name("")
                    .lat(0)
                    .lon(0)
                    .build();
        }
        return EventResponseDto.builder()
                .eventId(event.getId())
                .eventStatus(event.getEventStatus())
                .pageStatus(event.getPageStatus())
                .eventName(eventFirstPageDetails != null ? eventFirstPageDetails.getName() : null)
                .eventCoverImgUrl(eventSecondPageDetails != null ? eventSecondPageDetails.getEventCoverPage() : null)
                .startDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartDate() : null)
                .endDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventEndDate() : null)
                .startTime(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartTime() : null)
                .category(eventFirstPageDetails != null && eventFirstPageDetails.getEventCategory() != null ? eventFirstPageDetails.getEventCategory().getTitle() : null)
                .ticketType(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null && eventThirdPageDetails.getEventTicket().getTicketType() != null ? eventThirdPageDetails.getEventTicket().getTicketType().getTitle() : null)
                .ticketPrice(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketPrice() : null)
                .ticketsForSale(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketQuantity() : null)
                .ticketsSold(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketSold() : null)
                .organizerName(event.getEventOrganizer() != null ? event.getEventOrganizer().getUsername() : null)
                .country(physicalLocationDetails.getCountry())
                .location_display_name(physicalLocationDetails.getDisplayName())
                .lat(physicalLocationDetails.getLat())
                .lon(physicalLocationDetails.getLon())
                .build();
    }


    @Override
    public User getUserByUserId(int userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void followUser(int vendorId) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User followedBy =getUserByUsername(username);
        User followedTo = getUserByUserId(vendorId);
        vendorFollowerService.addFollower(followedTo,followedBy);
    }

    @Override
    public void unFollowUser(int vendorId) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User followedBy =getUserByUsername(username);
        vendorFollowerService.removeFollower(vendorId, followedBy.getUserId());
    }

    @Override
    public List<VendorResponseDto> getAllFollowing() {

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user =getUserByUsername(username);
        List<VendorFollowers> vendorFollowers= vendorFollowerService.getAllUserFollowings(user.getUserId());
        List<VendorResponseDto> vendorResponseDtos = new ArrayList<>();

        for (VendorFollowers vendorFollower:
             vendorFollowers) {

            User vendor= vendorFollower.getFollowedTo();

            vendorResponseDtos.add(

                    VendorResponseDto.builder()
                            .vendorId(vendor.getUserId())
                            .vendorName(vendor.getUsername())
                            .vendorFollowers(vendorFollowerService.getNoOfFollowers(vendor.getUserId()))
                            .vendorProfile(user.getUserDp())
                            .hasFollowed(vendorFollowerService.checkIfHasFollowedVendor(vendor.getUserId(), user.getUserId()))
                            .build()
            );
        }
        return vendorResponseDtos;
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
    public UserProfileDetailsResponse getUserProfileByUserId(int userId, HttpServletRequest request) {
        String accessBy= jwtUtils.customFilterCheck(request);
        User user = userRepository.findByUserId(userId);
        return changeToUserProfileResponse(accessBy, user);
    }


    @Override
    public UserProfileDetailsResponse getUserProfile() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByUsername(username);
        return changeToUserProfileResponse(username, user);
    }

    UserProfileDetailsResponse changeToUserProfileResponse(String accessedBy, User profileOf){
        List<Event> userEvents = eventRepository.getEventsForUserProfile(profileOf);

        String instagramLink=null;
        String facebookLink=null;
        String linkedInLink=null;
        String twitterLink=null;

        UserSocials userSocials= userSocailRepository.findUserSocialsByUser_UserId(profileOf.getUserId());

        if(userSocials!=null){
            instagramLink= userSocials.getInstagramLink();
            facebookLink= userSocials.getFacebookLink();
            linkedInLink= userSocials.getLinkedInLink();
            twitterLink = userSocials.getTwitterLink();
        }
        EditProfileDetails editProfileDetails= EditProfileDetails.builder().
                username(profileOf.getUsername()).
                userBio(profileOf.getUserBio())
                .profileDp(profileOf.getUserDp())
                .address(profileOf.getAddress())
                .phoneNumber(profileOf.getPhoneNumber())
                .email(profileOf.getEmail())
                .facebookLink(facebookLink)
                .instagramLink(instagramLink)
                .linkedInLink(linkedInLink)
                .twitterLink(twitterLink)
                .build();


        return UserProfileDetailsResponse.builder()
                .userSnippetDetails(editProfileDetails)
                .noOfEvents(userEvents.size())
                .pastEvents(
                        userEvents.stream()
                                .filter(each-> each.getEventFirstPageDetails().getEventDate().getEventEndDate().isBefore(LocalDate.now()))
                                .map(each-> changeToEventDto(each, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(each.getEventFirstPageDetails().getEventLocation())))
                                .toList()
                ).
                upcomingEvents(
                        userEvents.stream()
                                .filter(each-> each.getEventFirstPageDetails().getEventDate().getEventEndDate().isAfter(LocalDate.now()))
                                .map(each-> changeToEventDto(each, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(each.getEventFirstPageDetails().getEventLocation())))
                                .toList())
                .isUserProfile(accessedBy.equals(profileOf.getUsername()))
                .build();
    }

    @Override
    public EditProfileDetails getEditProfileDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user= getUserByUsername(username);

        String instagramLink=null;
        String facebookLink=null;
        String linkedInLink=null;
        String twitterLink=null;

        UserSocials userSocials= userSocailRepository.findUserSocialsByUser_UserId(user.getUserId());

        if(userSocials!=null){
            instagramLink= userSocials.getInstagramLink();
            facebookLink= userSocials.getFacebookLink();
            linkedInLink= userSocials.getLinkedInLink();
            twitterLink = userSocials.getTwitterLink();
        }
        return EditProfileDetails
                .builder()
                .profileDp(user.getUserDp())
                .address(user.getAddress())
                .username(username)
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .userBio(user.getUserBio())
                .facebookLink(facebookLink)
                .instagramLink(instagramLink)
                .linkedInLink(linkedInLink)
                .twitterLink(twitterLink)
                .build();
    }

    @Override
    public void editProfile(EditProfileDetails editProfileDetails) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        User user= getUserByUsername(username);
        UserSocials userSocials= userSocailRepository.findUserSocialsByUser_UserId(user.getUserId());

        if(userSocials==null) userSocials= new UserSocials();

        String userDp = null;
        if(editProfileDetails.getProfileDp()!=null){
            if(editProfileDetails.getProfileDp() instanceof MultipartFile){
                userDp= cloudinary.uploadImage((MultipartFile) editProfileDetails.getProfileDp(), "User Dps");
            }else {
                userDp= user.getUserDp();
            }
        }

        log.info("Phone NUmber"+editProfileDetails.getPhoneNumber());
        user.setUserDp(userDp);
        user.setUserBio(editProfileDetails.getUserBio());
        user.setUsername(editProfileDetails.getUsername());
        user.setPhoneNumber(editProfileDetails.getPhoneNumber());
        user.setAddress(editProfileDetails.getAddress());

        saveUser(
                user
        );

        userSocials.setUser(user);
        userSocials.setFacebookLink(editProfileDetails.getFacebookLink());
        userSocials.setInstagramLink(editProfileDetails.getInstagramLink());
        userSocials.setLinkedInLink(editProfileDetails.getLinkedInLink());
        userSocials.setTwitterLink(editProfileDetails.getTwitterLink());
        userSocailRepository.save(userSocials);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid user"));
    }
}
