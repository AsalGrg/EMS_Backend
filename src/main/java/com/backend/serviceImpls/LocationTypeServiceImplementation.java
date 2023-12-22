package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.LocationType;
import com.backend.repositories.LocationTypeRepository;
import com.backend.services.LocationTypeService;
import org.springframework.stereotype.Service;

@Service
public class LocationTypeServiceImplementation implements LocationTypeService {

    private LocationTypeRepository locationTypeRepository;

    public LocationTypeServiceImplementation(
            LocationTypeRepository locationTypeRepository
    ){
        this.locationTypeRepository= locationTypeRepository;
    }

    @Override
    public LocationType getLocationTypeByName(String name) {
        return locationTypeRepository.getLocationTypeByName(name).
                orElseThrow(()-> new ResourceNotFoundException("Invalid location type!"));
    }
}
