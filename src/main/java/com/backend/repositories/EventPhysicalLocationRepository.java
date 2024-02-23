package com.backend.repositories;

import com.backend.dtos.addEvent.EventPhysicalLocationDetailsDto;
import com.backend.models.EventLocation;
import com.backend.models.EventPhysicalLocationDetails;

public interface EventPhysicalLocationRepository {

    EventPhysicalLocationDetails getEventPhysicalLocationByEventLocation(EventLocation eventLocation);

    void savePhysicalLocationDetails(EventPhysicalLocationDetails eventPhysicalLocationDetails);
}
