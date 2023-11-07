package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Role;
import com.backend.repositories.RoleRepository;
import com.backend.services.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;

    public  RoleServiceImplementation (RoleRepository roleRepository){
        this.roleRepository= roleRepository;
    }

    @Override
    public Role findRoleByTitle(String title) {
        return roleRepository.findByTitle(title).orElseThrow(()-> new ResourceNotFoundException("Role not found!"));
    }
}
