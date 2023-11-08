package com.backend.repositories;

import com.backend.models.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType , Integer> {

    Optional<EventType> getEventTypeByTitle(String  title);
}
