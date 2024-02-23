package com.backend.serviceImpls;

import com.backend.dtos.addEvent.EventPhysicalLocationDetailsDto;
import com.backend.models.EventLocation;
import com.backend.models.EventPhysicalLocationDetails;
import com.backend.repositories.EventPhysicalLocationRepository;
import com.backend.services.EventPhysicalLocationDetailsService;
import org.springframework.stereotype.Service;

@Service
public class EventPhysicalLocationDetailsServiceImpl implements EventPhysicalLocationDetailsService {

    private EventPhysicalLocationRepository eventPhysicalLocationRepo;

    public EventPhysicalLocationDetailsServiceImpl(
            EventPhysicalLocationRepository eventPhysicalLocationRepo
    ){
        this.eventPhysicalLocationRepo= eventPhysicalLocationRepo;
    }
    @Override
    public EventPhysicalLocationDetails getEventPhysicalLocationDetailsByEventLocation(EventLocation eventLocation) {
        return eventPhysicalLocationRepo.getEventPhysicalLocationByEventLocation(eventLocation);
    }

    @Override
    public void savePhysicalLocationDetails(EventPhysicalLocationDetailsDto eventPhysicalLocationDetailsDto, EventLocation eventLocation) {
        eventPhysicalLocationRepo.savePhysicalLocationDetails(
                EventPhysicalLocationDetails.builder().
                        lat(eventPhysicalLocationDetailsDto.getLat()).
                        lon(eventPhysicalLocationDetailsDto.getLon()).
                        country(eventPhysicalLocationDetailsDto.getCountry()).
                        displayName(eventPhysicalLocationDetailsDto.getDisplayName()).
                        eventLocation(eventLocation).
                        build()
        );
    }
}
