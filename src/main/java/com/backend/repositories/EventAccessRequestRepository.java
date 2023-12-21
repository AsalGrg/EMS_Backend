package com.backend.repositories;

import com.backend.models.Event;
import com.backend.models.EventAccessRequest;
import com.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface EventAccessRequestRepository{

    void saveEventAccessRequest(EventAccessRequest eventAccessRequest);

    Optional<EventAccessRequest> getByUserAndEvent(User user, Event event);

    List<EventAccessRequest> findEventAccessRequestByEventOrganizer(String eventOrganizer);
}
