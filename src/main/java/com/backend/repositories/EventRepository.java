package com.backend.repositories;

import com.backend.models.*;
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

    List<Event> getOnlineEvents(String eventTitle);

    List<Event> getPhysicalEvents(String eventTitle, String eventCountry);

    boolean existsByName(String name);

    boolean existsByNameButNotForDraft(String name, Integer eventId);

    List<Event> getEventsByUser(User user);

    List<Event> getQuickSearchResult(String keyword);

    EventFirstPageDetails saveFirstPageDetails(EventFirstPageDetails eventFirstPageDetails);

    EventSecondPageDetails saveSecondPageDetails(EventSecondPageDetails eventSecondPageDetails);

    EventThirdPageDetails saveThirdPageDetails(EventThirdPageDetails eventThirdPageDetails);
}
