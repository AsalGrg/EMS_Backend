package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.models.EventAccessRequest;
import com.backend.models.User;
import com.backend.repositories.EventAccessRequestRepository;
import com.backend.services.EventAccessRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAccessRequestServiceImplementation implements EventAccessRequestService {

    private final EventAccessRequestRepository eventAccessRequestRepository;

    private EventAccessRequestServiceImplementation(EventAccessRequestRepository eventAccessRequestRepository){
        this.eventAccessRequestRepository= eventAccessRequestRepository;
    }

    @Override
    public void saveEventAccessRequest(EventAccessRequest eventAccessRequest) {
        eventAccessRequestRepository.save(eventAccessRequest);
    }

    @Override
    public List<EventAccessRequest> getEventsAccessRequestByEventOrg(String organizerName){
        List<EventAccessRequest> eventAccessRequests= eventAccessRequestRepository.findByEvent_EventOrganizer_Username(organizerName);

        if(eventAccessRequests.isEmpty()) throw new ResourceNotFoundException("No requests at the moment");
        return eventAccessRequests;
    }

    @Override
    public EventAccessRequest findRequestByUserAndEvent(User user, Event event){
        return eventAccessRequestRepository.findByUserAndEvent(user, event);
    }
}
