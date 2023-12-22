package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.VisibilityType;
import com.backend.repositories.VisibilityTypeRepository;
import com.backend.services.VisibilityTypeService;
import org.springframework.stereotype.Service;

@Service
public class VisibilityTypeServiceImplementation implements VisibilityTypeService {

    private VisibilityTypeRepository visibilityTypeRepository;

    public VisibilityTypeServiceImplementation(
            VisibilityTypeRepository visibilityTypeRepository
    ){
        this.visibilityTypeRepository = visibilityTypeRepository;
    }

    @Override
    public VisibilityType findVisibilityTypeByName(String name) {
        return visibilityTypeRepository.getVisibilityTypeByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid visibility type!"));
    }
}
