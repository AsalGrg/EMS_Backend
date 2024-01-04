package com.backend.services;

import com.backend.models.EventCategory;

public interface EventCategoryService {

    EventCategory getEventCategoryByName(String name);
}
