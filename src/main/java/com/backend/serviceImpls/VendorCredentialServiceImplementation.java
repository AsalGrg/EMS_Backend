package com.backend.serviceImpls;

import com.backend.dtos.VendorRequestsDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
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

import java.util.ArrayList;
import java.util.List;


@Service
public class VendorCredentialServiceImplementation implements VendorCredentialService {

    private VendorCredentialsRepository vendorCredentialsRepo;
    private UserRepository userRepository;


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


    public VendorCredentialServiceImplementation(VendorCredentialsRepository vendorCredentialsRepository, UserRepository userRepository){
        this.vendorCredentialsRepo= vendorCredentialsRepository;
        this.userRepository= userRepository;
    }
    @Override
    public VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq) {

        User user= checkUserCredentials(vendorRegistrationReq.getUsername(), "USER");

        VendorCredential vendorCredential= new VendorCredential();
        vendorCredential.setUser(user);
        //other vendor related setting to be done ... to be continued

        VendorCredential savedCredentials= this.vendorCredentialsRepo.save(vendorCredential);

        if(savedCredentials.getCredential_id()!=null) return new VendorRegistrationResponse("Your request has been collected. We will get back to you soon!");

        return null;
    }

    @Override
    public List<VendorRequestsDto> getVendorRequests(String username) {

        User user= checkUserCredentials(username, "ADMIN");

        List<VendorCredential> vendorRequests= this.vendorCredentialsRepo.findByIsVerifiedAndIsDeclined(false, false).
                orElseThrow(()-> new ResourceNotFoundException(("No requests at the moment")));

        List<VendorRequestsDto> vendorRequestsView= new ArrayList<>();

        for(VendorCredential vendorCredential: vendorRequests){
            VendorRequestsDto vendorRequestsDto= new VendorRequestsDto();

            vendorRequestsDto.setUsername(vendorCredential.getUser().getUsername());
            vendorRequestsDto.setCredential_id(vendorCredential.getCredential_id());
            vendorRequestsDto.setEmail(vendorCredential.getUser().getEmail());
            vendorRequestsDto.setAddress(vendorCredential.getUser().getAddress());
            vendorRequestsDto.setDeclined(vendorCredential.isDeclined());
            vendorRequestsDto.setVerified(vendorCredential.isVerified());

            vendorRequestsView.add(vendorRequestsDto);
        }


        return vendorRequestsView;
    }
}
