package com.backend.serviceImpls;

import com.backend.dtos.VendorDetailViewDto;
import com.backend.dtos.VendorRequestsDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Role;
import com.backend.models.User;
import com.backend.models.VendorCredential;
import com.backend.repositories.RoleRepository;
import com.backend.repositories.VendorCredentialsRepository;
import com.backend.services.VendorCredentialService;
import com.backend.utils.EmailMessages;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class VendorCredentialServiceImplementation implements VendorCredentialService {

    private final VendorCredentialsRepository vendorCredentialsRepo;

    private final UserServiceImplementation userService;

    private final RoleRepository roleRepository;

    private final CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl;

    private final EmailServiceImplementation emailServiceImplementation;
    private final EmailMessages emailMessages;

    public VendorCredentialServiceImplementation
            (VendorCredentialsRepository vendorCredentialsRepository, UserServiceImplementation userService, RoleRepository roleRepository,
             CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl, EmailServiceImplementation emailServiceImplementation, EmailMessages emailMessages){
        vendorCredentialsRepo= vendorCredentialsRepository;
        this.userService= userService;
        this.roleRepository= roleRepository;
        this.cloudinaryUploadServiceImpl= cloudinaryUploadServiceImpl;
        this.emailServiceImplementation =emailServiceImplementation;
        this.emailMessages= emailMessages;
    }

    VendorDetailViewDto changeToVendorViewDto(VendorCredential vendorCredential){

       return   VendorDetailViewDto
               .builder()
               .vendorName(vendorCredential.getUser().getUsername())
               .facebookLink(vendorCredential.getFacebookLink())
               .instagramLink(vendorCredential.getInstagramLink())
               .tiktokLink(vendorCredential.getTiktokLink())
               .linkedInLink(vendorCredential.getLinkedinLink())
               .ratings(vendorCredential.getRating())
               .businessEmail(vendorCredential.getBusinessEmail())
               .contactNumber1(vendorCredential.getContactNumber1())
               .contactNumber2(vendorCredential.getContactNumber2())
               .build();
    }

    @Override
    public VendorCredential findVendorCredentialByUser(User user){
        return vendorCredentialsRepo.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid vendor"));
    }


    //service Method for getting all the vendors
    @Override
    public List<VendorDetailViewDto> getAllVendors(){
        List<VendorCredential> allVendors= vendorCredentialsRepo.findByIsVerifiedAndIsDeclined(true, false);

        if(allVendors.isEmpty()){
            throw new ResourceNotFoundException("No vendors at the moment!");
        }
        List<VendorDetailViewDto> allVendorsView= new ArrayList<>();

        for(VendorCredential vendorCredential: allVendors){
            allVendorsView.add(changeToVendorViewDto(vendorCredential));
        }

        return allVendorsView;
    }

    public VendorCredential saveVendorCredentials(VendorCredential vendorCredential){
        return vendorCredentialsRepo.save(vendorCredential);
    }

    //method for checking the user credentials
    public User checkUserCredentials(String username, String role){

        if(username==null){
            throw new NotAuthorizedException();
        }

        User user = userService.getUserByUsername(username);

        for(Role eachRole: user.getUserRoles()){
            if(eachRole.getTitle().equals(role)){
                return user;
            }
        }

        throw new NotAuthorizedException("Access Denied: You do not have the necessary permissions to perform this action.");
    }



    @Override
    public VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq) {

        if (vendorCredentialsRepo.existsByUser_Username(vendorRegistrationReq.getUsername())) {
            throw new ResourceAlreadyExistsException("Your request has been collected already");
        }

        //saving the files of the vendors in the cloudinary as
        String taxClearanceCertificateUrl =  cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getTaxClearanceCertificate(), "Vendor Documents");
        String vendorRegistrationDocumentUrl =  cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getVendorRegistrationDocument(), "Vendor Documents");
        String vendorRegistrationFilledFormUrl =  cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getVendorRegistrationFilledForm(), "Vendor Documents");

        User user= checkUserCredentials(vendorRegistrationReq.getUsername(), "USER");



        //setting the social media fields of the vendor
        String facebookLink= null;
        String instagramLink= null;
        String tiktokLink= null;
        String linkedInLink= null;

        List<String> vendorSocialMediaLinks= vendorRegistrationReq.getSocialMediaLinks();
        for(int i=0;i<1; i++){
            //in list first: facebook, second: instgram, third: tiktok, last: linkedIn
            facebookLink= vendorSocialMediaLinks.get(0);
            instagramLink= vendorSocialMediaLinks.get(1);
            tiktokLink= vendorSocialMediaLinks.get(2);
            linkedInLink = vendorSocialMediaLinks.get(3);
        }

        //setting the contacts fields of the vendor
        String contactNumber1= null;
        String contactNumber2= null;
        List<String> vendorContacts = vendorRegistrationReq.getSocialMediaLinks();
        for(int i=0;i<1; i++){
            //in list first: facebook, second: instagram, third: TikTok, last: LinkedIn
            contactNumber1= vendorContacts.get(0);
            contactNumber2= vendorContacts.get(1);
        }

        VendorCredential vendorCredential=VendorCredential
                .builder()
                .user(user)
                .taxClearanceCertificate(taxClearanceCertificateUrl)
                .vendorRegistrationDocument(vendorRegistrationDocumentUrl)
                .vendorRegistrationFilledForm(vendorRegistrationFilledFormUrl)
                .businessEmail(vendorRegistrationReq.getBusinessEmail())
                .vendorDescription(vendorRegistrationReq.getVendorDescription())
                .facebookLink(facebookLink)
                .instagramLink(instagramLink)
                .tiktokLink(tiktokLink)
                .linkedinLink(linkedInLink)
                .contactNumber1(contactNumber1)
                .contactNumber2(contactNumber2)
                .build();

        VendorCredential savedCredentials= vendorCredentialsRepo.save(vendorCredential);

        if(savedCredentials.getCredential_id()!=null) return new VendorRegistrationResponse("Your request has been collected. We will get back to you soon!");

        throw new InternalServerError();
    }

    //service method to get all the become vendor requests dto.qqqqqq
    @Override
    public List<VendorRequestsDto> getVendorRequests(String username) {

        checkUserCredentials(username, "ADMIN");

        List<VendorCredential> vendorRequests= vendorCredentialsRepo.findByIsVerifiedAndIsDeclined(false, false);

        if(vendorRequests.isEmpty()){
            throw new ResourceNotFoundException("No requests at the moment");
        }

        List<VendorRequestsDto> vendorRequestsView= new ArrayList<>();

        for(VendorCredential vendorCredential: vendorRequests){
            VendorRequestsDto vendorRequestsDto = getVendorRequestsDto(vendorCredential);
            vendorRequestsView.add(vendorRequestsDto);
        }

        return vendorRequestsView;
    }


    private static VendorRequestsDto getVendorRequestsDto(VendorCredential vendorCredential) {
        return VendorRequestsDto
                .builder()
                .vendorName(vendorCredential.getUser().getUsername())
                .businessEmail(vendorCredential.getBusinessEmail())
                .vendorDescription(vendorCredential.getVendorDescription())
                .address(vendorCredential.getUser().getAddress())
                .vendorRegistrationDocument(vendorCredential.getVendorRegistrationDocument())
                .taxClearanceCertificate(vendorCredential.getTaxClearanceCertificate())
                .vendorRegistrationFilledForm(vendorCredential.getVendorRegistrationFilledForm())
                .build();
    }


    //to get the vendors that are terminated

    @Override
    public List<VendorRequestsDto> getTerminatedVendors(String username){

        checkUserCredentials(username, "ADMIN");

        List<VendorCredential> terminatedVendorCredentials = vendorCredentialsRepo.findByIsTerminated(true);

        if(terminatedVendorCredentials.isEmpty()) {
            throw new ResourceNotFoundException("No vendors to shows at the moment");
        }


        List<VendorRequestsDto> terminatedVendorsView= new ArrayList<>();

        for(VendorCredential vendorCredential: terminatedVendorCredentials){
            VendorRequestsDto vendorRequestsDto = getVendorRequestsDto(vendorCredential);

            terminatedVendorsView.add(vendorRequestsDto);
        }

        return terminatedVendorsView;
    }


    @Override
    public void vendorRequestAction(String username, String vendorName,String action) {
        checkUserCredentials(username,"ADMIN");

        User user= userService.getUserByUsername(vendorName);

        VendorCredential  vendorCredential= findVendorCredentialByUser(user);

        switch (action) {
            case "verify" -> {
                vendorCredential.setVerified(true);
                List<Role> userRoles= vendorCredential.getUser().getUserRoles();

                roleRepository.findByTitle("VENDOR").ifPresentOrElse(userRoles::add, ()->{
                    Role role= Role
                            .builder()
                            .title("VENDOR")
                            .description("Event Vendor")
                            .build();
                    //adding the v
                    userRoles.add(roleRepository.save(role));
                });

                //updating the user's role adding vendor role too.
                user.setUserRoles(userRoles);
                userService.saveUser(user);


                //after the vendor is approved sending email to the vendor
                Map<String, String> vendorAcceptanceMessageAndSubject = emailMessages.vendorAcceptanceMessage(vendorCredential.getUser().getUsername());
                String subject= vendorAcceptanceMessageAndSubject.get("subject");
                String body= vendorAcceptanceMessageAndSubject.get("message");

                //for now making the reciever me, to check only. Should pass the vendor email instead
                emailServiceImplementation.sendEmail("asal.gurung.a21.2@icp.edu.np", subject, body);
            }

            case "decline" -> {
                vendorCredential.setDeclined(true);

                //after the vendor is declined, sending email to the vendor
                Map<String, String> vendorAcceptanceMessageAndSubject = emailMessages.vendorDeclinedMessage(vendorCredential.getUser().getUsername());
                String subject= vendorAcceptanceMessageAndSubject.get("subject");
                String body= vendorAcceptanceMessageAndSubject.get("message");

                //for now making the reciever me, to check only. Should pass the vendor email instead
                emailServiceImplementation.sendEmail("asal.gurung.a21.2@icp.edu.np", subject, body);
            }

            //this conditions for further vendor actions such as terminating them etc.
            case "terminate" -> vendorCredential.setTerminated(true);
            case "commence" -> vendorCredential.setTerminated(false);

            default -> throw new InternalServerError("Invalid vendor action");
        }

        saveVendorCredentials(vendorCredential);
    }
}
