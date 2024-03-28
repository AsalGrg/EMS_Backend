package com.backend.services;

import com.backend.dtos.addEvent.EventPhysicalLocationDetailsDto;
import com.backend.models.EventLocation;
import com.backend.models.EventPhysicalLocationDetails;

public interface EventPhysicalLocationDetailsService {


    EventPhysicalLocationDetails getEventPhysicalLocationDetailsByEventLocation(EventLocation eventLocation);
    void savePhysicalLocationDetails(EventPhysicalLocationDetailsDto eventPhysicalLocationDetailsDto, EventLocation eventLocation);
    void updatePhysicalLocationDetails(EventPhysicalLocationDetails eventPhysicalLocationDetails);
}
