package com.backend.services;

import com.backend.models.VisibilityType;

public interface VisibilityTypeService {

    VisibilityType findVisibilityTypeByName(String name);
}
