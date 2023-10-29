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
import com.backend.repositories.UserRepository;
import com.backend.repositories.VendorCredentialsRepository;
import com.backend.services.VendorCredentialService;
import com.backend.utils.EmailMessages;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class VendorCredentialServiceImplementation implements VendorCredentialService {

    private VendorCredentialsRepository vendorCredentialsRepo;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl;

    private EmailServiceImplementation emailServiceImplementation;
    private EmailMessages emailMessages;

    public VendorCredentialServiceImplementation
            (VendorCredentialsRepository vendorCredentialsRepository, UserRepository userRepository, RoleRepository roleRepository,
             CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl, EmailServiceImplementation emailServiceImplementation, EmailMessages emailMessages){
        this.vendorCredentialsRepo= vendorCredentialsRepository;
        this.userRepository= userRepository;
        this.roleRepository= roleRepository;
        this.cloudinaryUploadServiceImpl= cloudinaryUploadServiceImpl;
        this.emailServiceImplementation =emailServiceImplementation;
        this.emailMessages= emailMessages;
    }

    VendorDetailViewDto changeToVendorViewDto(VendorCredential vendorCredential){

       VendorDetailViewDto vendorDetailViewDto=  new VendorDetailViewDto();
       vendorDetailViewDto.setVendorName(vendorCredential.getUser().getUsername());
       vendorDetailViewDto.setVendorDescription(vendorCredential.getVendorDescription());
       vendorDetailViewDto.setFacebookLink(vendorCredential.getFacebookLink());
       vendorDetailViewDto.setInstagramLink(vendorCredential.getInstagramLink());
       vendorDetailViewDto.setTiktokLink(vendorCredential.getTiktokLink());
       vendorDetailViewDto.setLinkedInLink(vendorCredential.getLinkedinLink());
       vendorDetailViewDto.setRatings(vendorCredential.getRating());
       vendorDetailViewDto.setBusinessEmail(vendorCredential.getBusinessEmail());
       vendorDetailViewDto.setContactNumber1(vendorCredential.getContactNumber1());
       vendorDetailViewDto.setContactNumber2(vendorCredential.getContactNumber2());

       return vendorDetailViewDto;
    }

    //service Method for getting all the vendors
    public List<VendorDetailViewDto> getAllVendors(){
        List<VendorCredential> allVendors= this.vendorCredentialsRepo.findByIsVerifiedAndIsDeclined(true, false).get();

        if(allVendors.isEmpty()){
            throw new ResourceNotFoundException("No vendors at the moment!");
        }
        List<VendorDetailViewDto> allVendorsView= new ArrayList<>();

        for(VendorCredential vendorCredential: allVendors){
            allVendorsView.add(changeToVendorViewDto(vendorCredential));
        }

        return allVendorsView;
    }
    //method for checking the user credentials
    public User checkUserCredentials(String username, String role){

        if(username==null){
            throw new NotAuthorizedException();
        }

        User user = this.userRepository.findByUsername(username).
                orElseThrow(()-> new ResourceNotFoundException("Invalid user"));

        for(Role eachRole: user.getUserRoles()){
            if(eachRole.getTitle().equals(role)){
                return user;
            }
        }

        throw new NotAuthorizedException("Access Denied: You do not have the necessary permissions to perform this action.");
    }



    @Override
    public VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq) {

        if (this.vendorCredentialsRepo.existsByUser_Username(vendorRegistrationReq.getUsername())) {
            throw new ResourceAlreadyExistsException("Your request has been collected already");
        }

        //saving the files of the vendors in the cloudinary as
        String taxClearanceCertificateUrl =  this.cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getTaxClearanceCertificate(), "Vendor Documents");
        String vendorRegistrationDocumentUrl =  this.cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getVendorRegistrationDocument(), "Vendor Documents");
        String vendorRegistrationFilledFormUrl =  this.cloudinaryUploadServiceImpl.uploadImage(vendorRegistrationReq.getVendorRegistrationFilledForm(), "Vendor Documents");

        User user= checkUserCredentials(vendorRegistrationReq.getUsername(), "USER");

        VendorCredential vendorCredential= new VendorCredential();
        vendorCredential.setUser(user);

        //setting the urls where the documents are stored
        vendorCredential.setTaxClearanceCertificate(taxClearanceCertificateUrl);
        vendorCredential.setVendorRegistrationDocument(vendorRegistrationDocumentUrl);
        vendorCredential.setVendorRegistrationFilledForm(vendorRegistrationFilledFormUrl);
        //other vendor related setting to be done ... to be continued

        vendorCredential.setBusinessEmail(vendorRegistrationReq.getBusinessEmail());
        vendorCredential.setVendorDescription(vendorRegistrationReq.getVendorDescription());


        //setting the social media fields of the vendor
        List<String> vendorSocialMediaLinks= vendorRegistrationReq.getSocialMediaLinks();
        for(int i=0;i<1; i++){
            //in list first: facebook, second: instgram, third: tiktok, last: linkedIn
            vendorCredential.setFacebookLink(vendorSocialMediaLinks.get(0));
            vendorCredential.setInstagramLink(vendorSocialMediaLinks.get(1));
            vendorCredential.setTiktokLink(vendorSocialMediaLinks.get(2));
            vendorCredential.setLinkedinLink(vendorSocialMediaLinks.get(3));
        }

        //setting the contacts fields of the vendor
        List<String> vendorContacts = vendorRegistrationReq.getSocialMediaLinks();
        for(int i=0;i<1; i++){
            //in list first: facebook, second: instgram, third: tiktok, last: linkedIn
            vendorCredential.setContactNumber1(vendorContacts.get(0));
            vendorCredential.setContactNumber2(vendorContacts.get(1));
        }

        VendorCredential savedCredentials= this.vendorCredentialsRepo.save(vendorCredential);

        if(savedCredentials.getCredential_id()!=null) return new VendorRegistrationResponse("Your request has been collected. We will get back to you soon!");

        throw new InternalServerError();
    }

    //service method to get all the become vendor requests dto.qqqqqq
    @Override
    public List<VendorRequestsDto> getVendorRequests(String username) {

        User user= checkUserCredentials(username, "ADMIN");

        List<VendorCredential> vendorRequests= this.vendorCredentialsRepo.findByIsVerifiedAndIsDeclined(false, false).get();

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
        VendorRequestsDto vendorRequestsDto= new VendorRequestsDto();
        vendorRequestsDto.setVendorName(vendorCredential.getUser().getUsername());
        vendorRequestsDto.setBusinessEmail(vendorCredential.getBusinessEmail());
        vendorRequestsDto.setVendorDescription(vendorCredential.getVendorDescription());
        vendorRequestsDto.setAddress(vendorCredential.getUser().getAddress());

        vendorRequestsDto.setVendorRegistrationDocument(vendorCredential.getVendorRegistrationDocument());
        vendorRequestsDto.setTaxClearanceCertificate(vendorCredential.getTaxClearanceCertificate());
        vendorRequestsDto.setVendorRegistrationFilledForm(vendorCredential.getVendorRegistrationFilledForm());
        return vendorRequestsDto;
    }


    //to get the vendors that are terminated
    public List<VendorRequestsDto> getTerminatedVendors(String username){

        checkUserCredentials(username, "ADMIN");

        List<VendorCredential> terminatedVendorCredentials = this.vendorCredentialsRepo.findByIsTerminated(true).get();

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

        VendorCredential  vendorCredential= this.vendorCredentialsRepo.findByUser(this.userRepository.findByUsername(vendorName).orElseThrow(()->new ResourceNotFoundException("Username does not exist")))
                .orElseThrow(()->new ResourceNotFoundException("Invalid vendor name"));

        switch (action) {
            case "verify" -> {
                vendorCredential.setVerified(true);
                List<Role> userRoles= vendorCredential.getUser().getUserRoles();

                this.roleRepository.findByTitle("VENDOR").ifPresentOrElse(role -> userRoles.add(role), ()->{
                    Role role= new Role();
                    role.setTitle("VENDOR");
                    role.setDescription("Event Vendor");

                    this.roleRepository.save(role);

                    User vendorCredentials = userRepository.findByUsername(vendorName).get();
                    vendorCredentials.setUserRoles(userRoles);

                    this.userRepository.save(vendorCredentials);
                });

                //after the vendor is approved sending email to the vendor
                Map<String, String> vendorAcceptanceMessageAndSubject = this.emailMessages.vendorAcceptanceMessage(vendorCredential.getUser().getUsername());
                String subject= vendorAcceptanceMessageAndSubject.get("subject");
                String body= vendorAcceptanceMessageAndSubject.get("message");

                //for now making the reciever me, to check only. Should pass the vendor email instead
                this.emailServiceImplementation.sendEmail("asal.gurung.a21.2@icp.edu.np", subject, body);
            }

            case "decline" -> {
                vendorCredential.setDeclined(true);

                //after the vendor is declined, sending email to the vendor
                Map<String, String> vendorAcceptanceMessageAndSubject = this.emailMessages.vendorDeclinedMessage(vendorCredential.getUser().getUsername());
                String subject= vendorAcceptanceMessageAndSubject.get("subject");
                String body= vendorAcceptanceMessageAndSubject.get("message");

                //for now making the reciever me, to check only. Should pass the vendor email instead
                this.emailServiceImplementation.sendEmail("asal.gurung.a21.2@icp.edu.np", subject, body);
            }

            //this conditions for further vendor actions such as terminating them etc.
            case "terminate" -> vendorCredential.setTerminated(true);
            case "commence" -> vendorCredential.setTerminated(false);

            default -> throw new InternalServerError("Invalid vendor action");
        }

        this.vendorCredentialsRepo.save(vendorCredential);
    }
}
