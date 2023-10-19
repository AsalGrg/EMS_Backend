package com.backend.serviceImpls;

import com.backend.dtos.VendorRequestsDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Role;
import com.backend.models.User;
import com.backend.models.VendorCredential;
import com.backend.repositories.UserRepository;
import com.backend.repositories.VendorCredentialsRepository;
import com.backend.services.VendorCredentialService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;


@Service
public class VendorCredentialServiceImplementation implements VendorCredentialService {

    private VendorCredentialsRepository vendorCredentialsRepo;
    private UserRepository userRepository;

    public VendorCredentialServiceImplementation(VendorCredentialsRepository vendorCredentialsRepository, UserRepository userRepository){
        this.vendorCredentialsRepo= vendorCredentialsRepository;
        this.userRepository= userRepository;
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

        User user= checkUserCredentials(vendorRegistrationReq.getUsername(), "USER");

        VendorCredential vendorCredential= new VendorCredential();
        vendorCredential.setUser(user);
        //other vendor related setting to be done ... to be continued

        VendorCredential savedCredentials= this.vendorCredentialsRepo.save(vendorCredential);

        if(savedCredentials.getCredential_id()!=null) return new VendorRegistrationResponse("Your request has been collected. We will get back to you soon!");

        throw new InternalServerError();
    }

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
        vendorRequestsDto.setUsername(vendorCredential.getUser().getUsername());
        vendorRequestsDto.setCredential_id(vendorCredential.getCredential_id());
        vendorRequestsDto.setEmail(vendorCredential.getUser().getEmail());
        vendorRequestsDto.setAddress(vendorCredential.getUser().getAddress());
        vendorRequestsDto.setDeclined(vendorCredential.isDeclined());
        vendorRequestsDto.setVerified(vendorCredential.isVerified());
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
            case "verify" -> vendorCredential.setVerified(true);
            case "decline" -> vendorCredential.setDeclined(true);

            //this conditions for further vendor actions such as terminating them etc.
            case "terminate" -> vendorCredential.setTerminated(true);
            case "commence" -> vendorCredential.setTerminated(false);

            default -> throw new InternalServerError("Invalid vendor action");
        }

        this.vendorCredentialsRepo.save(vendorCredential);
    }
}
