package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.EventCategory;
import com.backend.repositories.EventCategoryRepository;
import com.backend.services.EventCategoryService;
import org.springframework.stereotype.Service;

@Service
public class EventCategoryServiceImplementation implements EventCategoryService {
    
    private EventCategoryRepository eventCategoryRepository;

    public EventCategoryServiceImplementation(
            EventCategoryRepository eventCategoryRepository
    ){
        this.eventCategoryRepository= eventCategoryRepository;
    }

    @Override
    public EventCategory getEventCategoryByName(String name) {
        return eventCategoryRepository.getEventCategoryByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid event category"));
    }
}
