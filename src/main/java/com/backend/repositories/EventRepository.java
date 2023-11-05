package com.backend.repositories;

import com.backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByLocationAndEventTimeAndEventDateAndEventType_TitleAndIsAccepted(String location, Time eventTime, LocalDate eventDate, String eventTitle, boolean isAccepted);
    Optional<Event>  findEventByName(String name);

    Event findEventById(int id);

    List<Event> findEventByLocationAndIsAcceptedAndIsDeclined(String location, boolean isAccepted, boolean isDeclined);

    @Query(value = "SELECT e.* from event e Inner JOIN event_type et ON e.event_type_id= et.id WHERE et.title =:name AND e.isAccepted= true AND e.isDeclined= false", nativeQuery = true)
    List<Event> findEventByType(@Param("name") String type);

    List<Event> findByIsAcceptedAndIsDeclined(boolean isAccepted, boolean isDeclined);

    List<Event> findAllByIsAcceptedAndEventDateAfterOrderByTicketSoldDesc(boolean isAccepted, LocalDate eventDateAfter);
    boolean existsByName(String name);

    Optional<Event> findByAccessToken(String accessToken);
}
