package com.backend.services;

import com.backend.models.EventLocation;

public interface EventLocationService {

    EventLocation saveEventLocation(String locationType, String eventLocation);

    EventLocation updateEventLocation(EventLocation eventLocation);
}
