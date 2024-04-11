package com.backend.repositories;

import com.backend.models.CollectionEvents;
import com.backend.models.Event;
import com.backend.models.EventCollection;

import java.util.List;

public interface EventCollectionRepository {

    EventCollection saveEventCollection(EventCollection eventCollection);
    List<EventCollection> getAllEventCollection();

    List<Event> getAllEventCollectionEvents(int collectionId);
    List<Event> getUpcomingEventsOfCollection( int eventCollectionId);

}
