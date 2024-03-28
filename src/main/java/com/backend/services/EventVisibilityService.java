package com.backend.services;

import com.backend.models.EventVisibility;

public interface EventVisibilityService {

    EventVisibility saveEventVisibility(String eventVisibilityType, String eventAccessPassword);
    EventVisibility updateEventVisibility(String eventVisibilityType, String eventAccessPassword, int visibilityId);
}
