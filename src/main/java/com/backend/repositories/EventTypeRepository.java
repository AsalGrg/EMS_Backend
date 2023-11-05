package com.backend.repositories;

import com.backend.models.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType , Integer> {

    EventType getEventTypeByTitle(String  title);
}
