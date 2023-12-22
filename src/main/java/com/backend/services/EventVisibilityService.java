package com.backend.services;

import com.backend.models.EventVisibility;

public interface EventVisibilityService {

    EventVisibility saveEventVisibility(String eventVisibilityType, String eventAccessPassword);
}
