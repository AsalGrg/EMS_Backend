package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.EventType;
import com.backend.repositories.EventTypeRepository;
import com.backend.services.EventTypeService;
import org.springframework.stereotype.Service;

@Service
public class EventTypeServiceImplementation implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    public EventTypeServiceImplementation(
            EventTypeRepository eventTypeRepository
    ) {
        this.eventTypeRepository= eventTypeRepository;
    }

    @Override
    public EventType getEventTypeByTitle(String title) {
        return eventTypeRepository.getEventTypeByTitle(title)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid event type"));
    }
}
