package com.backend.serviceImpls;

import com.backend.dtos.vendorRegistration.VendorRegistrationRequestDto;
import com.backend.dtos.vendorRegistration.VendorRegistrationResponse;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.models.User;
import com.backend.models.VendorCredential;
import com.backend.repositories.UserRepository;
import com.backend.repositories.VendorCredentialsRepository;
import com.backend.services.VendorCredentialService;
import org.springframework.stereotype.Service;


@Service
public class VendorCredentialServiceImplementation implements VendorCredentialService {

    private VendorCredentialsRepository vendorCredentialsRepo;
    private UserRepository userRepository;

    public VendorCredentialServiceImplementation(VendorCredentialsRepository vendorCredentialsRepository, UserRepository userRepository){
        this.vendorCredentialsRepo= vendorCredentialsRepository;
        this.userRepository= userRepository;
    }
    @Override
    public VendorRegistrationResponse becomeVendor(VendorRegistrationRequestDto vendorRegistrationReq) {

        User user= this.userRepository.findByUsername(vendorRegistrationReq.getUsername()).orElseThrow(
                NotAuthorizedException::new
        );

        VendorCredential vendorCredential= new VendorCredential();
        vendorCredential.setUser(user);
        //other vendor related setting to be done ... to be continued

        VendorCredential savedCredentials= this.vendorCredentialsRepo.save(vendorCredential);

        if(savedCredentials.getCredential_id()!=null) return new VendorRegistrationResponse("Your request has been collected. We will get back to you soon!");

        return null;
    }
}
