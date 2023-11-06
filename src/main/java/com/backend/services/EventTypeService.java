package com.backend.services;

import com.backend.models.EventType;

public interface EventTypeService {

    public EventType getEventTypeByTitle(String title);
}
