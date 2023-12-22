package com.backend.serviceImpls;

import com.backend.models.EventVisibility;
import com.backend.repositories.EventVisibilityRepository;
import com.backend.services.EventVisibilityService;
import com.backend.services.VisibilityTypeService;
import org.springframework.stereotype.Service;

@Service
public class EventVisibilityServiceImplementation implements EventVisibilityService {

    private EventVisibilityRepository eventVisibilityRepository;

    private VisibilityTypeService visibilityTypeService;

    public EventVisibilityServiceImplementation(
            EventVisibilityRepository eventVisibilityRepository,
            VisibilityTypeService visibilityTypeService
    ){
        this.eventVisibilityRepository = eventVisibilityRepository;
        this.visibilityTypeService= visibilityTypeService;
    }

    @Override
    public EventVisibility saveEventVisibility(String eventVisibilityType, String eventAccessPassword) {

        return eventVisibilityRepository.saveEventVisibility(
                EventVisibility
                        .builder()
                        .visibilityType(visibilityTypeService.findVisibilityTypeByName(eventVisibilityType))
                        .eventAccessPassword(eventAccessPassword)
                        .build()
        );
    }
}
