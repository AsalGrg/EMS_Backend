package com.backend.services;

import com.backend.models.Event;
import com.backend.models.EventAccessRequest;
import com.backend.models.User;

import java.util.List;

public interface EventAccessRequestService {

    void saveEventAccessRequest(EventAccessRequest eventAccessRequest);

    List<EventAccessRequest> getEventsAccessRequestByEventOrg(String organizerName);

    EventAccessRequest findRequestByUserAndEvent(User user, Event event);
}
