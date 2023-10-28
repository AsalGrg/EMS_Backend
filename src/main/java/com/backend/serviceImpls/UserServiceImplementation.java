package com.backend.serviceImpls;

import com.backend.dtos.login.LoginUserDto;
import com.backend.dtos.register.RegisterUserDto;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Role;
import com.backend.models.User;
import com.backend.repositories.RoleRepository;
import com.backend.repositories.UserRepository;
import com.backend.services.UserService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private CloudinaryUploadServiceImplementation cloudinary;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, RoleRepository roleRepository, CloudinaryUploadServiceImplementation cloudinary){
        this.userRepository= userRepository;
        this.roleRepository= roleRepository;
        this.cloudinary= cloudinary;
    }


    //here contains the overridden method from the UserDetailsService

    public User registerAdmin(RegisterUserDto registerUserDto){

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
        //encoding the password here and then storing it, to be continued..
        user.setPassword(registerUserDto.getPassword());

        //no events are there in the user at first
        user.setEvents(null);

        this.userRepository.save(user);

        return user;
    }




    //overridden method from the UserService interface
    @Override
    public User registerUser(RegisterUserDto registerUserDto) {

        boolean usernameExists= this.userRepository.existsByUsername(registerUserDto.getUsername());
        boolean emailExists= this.userRepository.existsByEmail(registerUserDto.getEmail());

        if(usernameExists){
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        if(emailExists){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        String userDpUrl= null;

        if (registerUserDto.getUserDp()!=null){
            userDpUrl= this.cloudinary.uploadImage(registerUserDto.getUserDp(), "User Dps");
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
        //encoding the password here and then storing it, to be continued..
        user.setPassword(registerUserDto.getPassword());

        //no events are there in the user at first
        user.setEvents(null);

        this.userRepository.save(user);

        return user;
    }

    @Override
    public User loginUser(LoginUserDto loginUserDto){

       User user = this.userRepository.findByEmailAndPassword(loginUserDto.getEmail(), loginUserDto.getPassword())
                .orElseThrow(()-> new ResourceNotFoundException("User with the given credentials does not exist!"));
        return user;
    }
}
