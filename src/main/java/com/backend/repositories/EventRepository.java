package com.backend.repositories;

import com.backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    public Optional<Event>  findEventByName(String name);

    public Event findEventById(int id);

    public List<Event> findEventByLocationAndIsAcceptedAndIsDeclined(String location, boolean isAccepted, boolean isDeclined);

    @Query(value = "SELECT e.* from event e Inner JOIN event_type et ON e.event_type_id= et.id WHERE et.title =:name AND e.isAccepted= true AND e.isDeclined= false", nativeQuery = true)
    public List<Event> findEventByType(@Param("name") String type);

    public List<Event> findByIsAcceptedAndIsDeclined(boolean isAccepted, boolean isDeclined);
    public boolean existsByName(String name);

    public Optional<Event> findByAccessToken(String accessToken);
}
