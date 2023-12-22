package com.backend.repositories;

import com.backend.models.Event;
import com.backend.models.EventCategory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository {

    Event getEventById(int id);

    Optional<Event> getEventByName(String name);

    Event saveEvent(Event event);

    List<Event> getEventByLocation(String location);

    List<Event> getEventByType(String type);

    List<Event> getAllEvents();

    List<Event> getEventByFilter(String location, LocalTime eventTime, LocalDate eventDate, EventCategory eventType);

    List<Event> getTrendingEvents(LocalDate date);

    boolean existsByName(String name);
//
//    List<Event> findByLocationAndEventTimeAndEventDateAndEventTypeAndIsAccepted(String location, Time eventTime, LocalDate eventDate, EventType eventType, boolean isAccepted);
//    Optional<Event>  findEventByName(String name);
//
//    Event findEventById(int id);
//
//    List<Event> findEventByLocationAndIsAcceptedAndIsDeclined(String location, boolean isAccepted, boolean isDeclined);
//
//    @Query(value = "SELECT e.* from event e Inner JOIN event_type et ON e.event_type_id= et.id WHERE et.title =:name AND e.isAccepted= true AND e.isDeclined= false", nativeQuery = true)
//    List<Event> findEventByType(@Param("name") String type);
//
//    List<Event> findByIsAcceptedAndIsDeclined(boolean isAccepted, boolean isDeclined);
//
//    List<Event> findAllByIsAcceptedAndEventDateAfterOrderByTicketSoldDesc(boolean isAccepted, LocalDate eventDateAfter);
//    boolean existsByName(String name);
//
//    Optional<Event> findByAccessToken(String accessToken);
}
