package com.backend.repositories;

import com.backend.models.EventCategory;

import java.util.List;
import java.util.Optional;

public interface EventCategoryRepository {

    List<String> getAllEventCategories();
    Optional<EventCategory> getEventCategoryByName (String name);
}
