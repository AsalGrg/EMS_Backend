package com.backend.services;

import com.backend.models.LocationType;

public interface LocationTypeService {

    LocationType getLocationTypeByName(String name);
}
