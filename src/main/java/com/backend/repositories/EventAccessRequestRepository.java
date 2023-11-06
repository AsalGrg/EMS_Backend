package com.backend.repositories;

import com.backend.models.Event;
import com.backend.models.EventAccessRequest;
import com.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventAccessRequestRepository extends JpaRepository<EventAccessRequest, Integer> {

    EventAccessRequest findByUserAndEvent(User user, Event event);

    List<EventAccessRequest> findByEvent_EventOrganizer_Username(String eventOrganizer);
}
