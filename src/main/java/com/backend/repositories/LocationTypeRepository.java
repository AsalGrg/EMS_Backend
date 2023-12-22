package com.backend.repositories;

import com.backend.models.LocationType;

import java.util.Optional;

public interface LocationTypeRepository {

    Optional<LocationType> getLocationTypeByName(String name);
}
