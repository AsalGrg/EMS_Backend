package com.backend.repositoryImpls;

import com.backend.models.*;
import com.backend.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Event getEventById(int id) {
        Session session= sessionFactory.openSession();
        Query<Event> query = session.createQuery("FROM Event et WHERE et.id =: id", Event.class);

        query.setParameter("id", id);
        Event event= query.uniqueResult();

        session.close();
        return  event;
    }

    @Override
    public Optional<Event> getEventByName(String name) {
        Session session= sessionFactory.openSession();
        Query<Event> query= session.createQuery("FROM Event et where et.eventFirstPageDetails.name =:name", Event.class);
        query.setParameter("name", name);

        Optional<Event> event= query.uniqueResultOptional();
        session.close();
        return event;
    }

    @Override
    public Event saveEvent(Event event) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

// Using merge to save or update the entity
        Event savedEvent = session.merge(event);

        tx.commit();
        session.close();

        return savedEvent;
    }

    @Override
    public EventFirstPageDetails saveFirstPageDetails(EventFirstPageDetails eventFirstPageDetails) {

        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventFirstPageDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventFirstPageDetails;
    }

    @Override
    public EventSecondPageDetails saveSecondPageDetails(EventSecondPageDetails eventSecondPageDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventSecondPageDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventSecondPageDetails;
    }

    @Override
    public EventThirdPageDetails saveThirdPageDetails(EventThirdPageDetails eventThirdPageDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventThirdPageDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventThirdPageDetails;
    }


    @Override
    public List<Event> getEventByLocation(String location) {
        Session session= sessionFactory.openSession();
        Query<Event> eventQuery = session.createQuery("FROM Event et JOIN event_physical_location_details ep ON et.eventFirstPageDetails.eventLocation.id= ep.eventLocation.id WHERE ep.country = :location AND et.isPrivate = false AND et.eventStatus= 'completed'", Event.class);
        eventQuery.setParameter("location", location);
        List<Event> events= eventQuery.getResultList();
        session.close();
        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        Session session = sessionFactory.openSession();
        Query<Event> query= session.createQuery("FROM Event er WHERE er.eventFirstPageDetails.eventCategory.title =:type ", Event.class);
        query.setParameter("type", type);
        List<Event> events= query.getResultList();
        session.close();
        return events;
    }

    @Override
    public List<Event> getAllEvents() {
        Session session= sessionFactory.openSession();
        Query<Event> query= session.createQuery("FROM Event er WHERE er.isPrivate = false ", Event.class);

        List<Event> events= query.getResultList();
        session.close();
        return events;
    }

    @Override
    public List<Event> getEventByFilter(String location, LocalTime eventTime, LocalDate eventDate, EventCategory eventType) {
        Session session= sessionFactory.openSession();
        String query = "FROM Event er WHERE er.eventFirstPageDetails.eventLocation.locationName=: location AND er.eventFirstPageDetails.eventDate.eventStartTime = :eventTime AND er.eventFirstPageDetails.eventDate.eventStartDate = :eventDate AND er.eventFirstPageDetails.eventCategory.title =: eventType AND er.isPrivate =: false";
        Query<Event> eventQuery = session.createQuery(query, Event.class);
        eventQuery.setParameter("location", location);
        eventQuery.setParameter("eventTime", eventTime);
        eventQuery.setParameter("eventDate", eventDate);
        eventQuery.setParameter("eventType", eventType);

        List<Event> events= eventQuery.getResultList();
        session.close();

        return  events;
    }

    @Override
    public List<Event> getTrendingEvents(LocalDate date) {
        Session session= sessionFactory.openSession();

        Query<Event> eventQuery= session.createQuery("FROM  Event er WHERE er.eventFirstPageDetails.eventDate.eventStartDate>= :date AND er.isPrivate =false ORDER BY er.eventThirdPageDetails.eventTicket.ticketSold DESC", Event.class);
        eventQuery.setParameter("date", date);
        List<Event> events= eventQuery.getResultList();
        session.close();
        return events;
    }

    @Override
    public List<Event> getOnlineEvents(String eventTitle) {
        Session session= sessionFactory.openSession();
        Query<Event> eventQuery= session.createQuery("FROM Event er WHERE er.eventFirstPageDetails.eventLocation.locationType.locationTypeTitle='online' AND er.eventFirstPageDetails.name=:eventTitle", Event.class);
        eventQuery.setParameter("eventTitle", eventTitle);
        List<Event> events = eventQuery.getResultList();
        return events;
    }

    @Override
    public List<Event> getPhysicalEvents(String eventTitle, String eventCountry) {
        Session session = sessionFactory.openSession();
        log.info(eventTitle);
        log.info(eventCountry);
        Query<Event> eventQuery= session.createQuery("FROM Event e JOIN  EventLocation  el ON e.eventFirstPageDetails.eventLocation.id = el.id LEFT JOIN event_physical_location_details epld ON el.id = epld.eventLocation.id WHERE e.eventFirstPageDetails.name = :eventTitle AND el.locationType.locationTypeTitle = 'venue' AND epld.country = :eventCountry", Event.class);
        eventQuery.setParameter("eventTitle", eventTitle);
        eventQuery.setParameter("eventCountry", eventCountry);
        return eventQuery.getResultList();
    }

    @Override
    public List<Event> getAllOnlineEvents() {
        Session session= sessionFactory.openSession();
        Query<Event> eventQuery= session.createQuery("FROM Event er WHERE er.eventFirstPageDetails.eventLocation.locationType.locationTypeTitle='online'", Event.class);
        return eventQuery.getResultList();
    }

    @Override
    public boolean existsByName(String name) {
        Session session= sessionFactory.openSession();
        log.info("EVENT NAME TO CHECK:"+ name);
        Query<Boolean> exitsByUsername = session.createQuery("SELECT CASE WHEN count (er)> 0 THEN true ELSE false END FROM EventFirstPageDetails er WHERE er.name= :name", Boolean.class);
        exitsByUsername.setParameter("name", name);

        return exitsByUsername.uniqueResult();
    }

    @Override
    public boolean existsByNameButNotForDraft(String name, Integer eventId) {
        Session session= sessionFactory.openSession();
        log.info("EVENT NAME TO CHECK:"+ name);
        Query<Boolean> exitsByUsername = session.createQuery("SELECT CASE WHEN count (er.eventFirstPageDetails)> 0 THEN true ELSE false END FROM Event er WHERE er.eventFirstPageDetails.name= :name AND er.id !=:eventId", Boolean.class);
        exitsByUsername.setParameter("name", name);
        exitsByUsername.setParameter("eventId", eventId);

        return exitsByUsername.uniqueResult();
    }

    @Override
    public List<Event> getEventsByUser(User user) {
        Session session= sessionFactory.openSession();

        Query<Event> getEventsByUser= session.createQuery("FROM Event e JOIN User u on e.eventOrganizer.id = u.id WHERE u.id =:user_id", Event.class);

        getEventsByUser.setParameter("user_id", user.getUser_id());
        return getEventsByUser.getResultList();
    }

    @Override
    public List<Event> getQuickSearchResult(String keyword) {

        Session session=  sessionFactory.openSession();
        Query<Event> getQuickResults= session.createQuery("FROM Event e JOIN event_physical_location_details epl ON e.eventFirstPageDetails.eventLocation.id = epl.eventLocation.id WHERE e.eventFirstPageDetails.name LIKE :eventName OR e.eventFirstPageDetails.eventCategory.title LIKE :eventTitle OR epl.displayName LIKE :eventLocation", Event.class);
        getQuickResults.setParameter("eventName", "%"+keyword+"%");
        getQuickResults.setParameter("eventTitle", "%"+keyword+"%");
        getQuickResults.setParameter("eventLocation", "%"+keyword+"%");
        return getQuickResults.getResultList();
    }



}
