package com.backend.services;

import com.backend.models.EventCategory;

import java.util.List;

public interface EventCategoryService {

    List<String> getAllEventCategories();
    EventCategory getEventCategoryByName(String name);
}
