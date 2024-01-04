package com.backend.repositories;

import com.backend.models.EventCategory;

import java.util.Optional;

public interface EventCategoryRepository {

    Optional<EventCategory> getEventCategoryByName (String name);
}
