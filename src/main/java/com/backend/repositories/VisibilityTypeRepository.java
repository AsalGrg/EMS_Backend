package com.backend.repositories;

import com.backend.models.VisibilityType;

import java.util.Optional;

public interface VisibilityTypeRepository {

    Optional<VisibilityType> getVisibilityTypeByName(String name);
}
