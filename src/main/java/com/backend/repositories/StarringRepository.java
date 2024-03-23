package com.backend.repositories;

import com.backend.models.EventStarring;

public interface StarringRepository {

    void saveStarring(EventStarring eventStarring);

    EventStarring getEventStarringByEventId(int eventId);
}
