package com.backend.serviceImpls;

import com.backend.models.EventLocation;
import com.backend.repositories.EventLocationRepository;
import com.backend.services.EventLocationService;
import com.backend.services.LocationTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        EventLocation eventLocationObj;

        log.info("location type", locationType);
        if(locationType.equals("Online")){
            log.info("ONLINE");
            eventLocationObj=EventLocation.
                    builder()
                    .locationName(eventLocation)
                    .locationType(locationTypeService.getLocationTypeByName(locationType))
                    .isPhysical(false)
                    .build();
        }
        else {

            log.info("PHYSICALL");
            eventLocationObj=EventLocation.
                    builder()
                    .locationName(eventLocation)
                    .locationType(locationTypeService.getLocationTypeByName(locationType))
                    .isPhysical(true)
                    .build();
        }
        return eventLocationRepository.saveEventLocation(
                eventLocationObj
        );
    }

    @Override
    public EventLocation updateEventLocation(EventLocation eventLocation) {
        return eventLocationRepository.saveEventLocation(eventLocation);
    }
}
