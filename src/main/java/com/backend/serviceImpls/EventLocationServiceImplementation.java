package com.backend.serviceImpls;

import com.backend.models.EventLocation;
import com.backend.repositories.EventLocationRepository;
import com.backend.services.EventLocationService;
import com.backend.services.LocationTypeService;
import org.springframework.stereotype.Service;

@Service
public class EventLocationServiceImplementation implements EventLocationService {

    private EventLocationRepository eventLocationRepository;

    private LocationTypeService locationTypeService;

    public EventLocationServiceImplementation(
            EventLocationRepository eventLocationRepository,
            LocationTypeService locationTypeService
    ){
        this.eventLocationRepository= eventLocationRepository;
        this.locationTypeService= locationTypeService;
    }

    @Override
    public EventLocation saveEventLocation(String locationType, String eventLocation) {

        return eventLocationRepository.saveEventLocation(
                EventLocation.
                        builder()
                        .locationName(eventLocation)
                        .locationType(locationTypeService.getLocationTypeByName(locationType))
                        .build()
        );
    }
}
