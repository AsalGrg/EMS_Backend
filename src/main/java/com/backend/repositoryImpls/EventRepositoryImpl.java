package com.backend.repositoryImpls;

import com.backend.models.Event;
import com.backend.models.EventType;
import com.backend.repositories.EventRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
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
        Query<Event> query= session.createQuery("FROM Event et where et.name =:name", Event.class);
        query.setParameter("name", name);

        Optional<Event> event= query.uniqueResultOptional();
        session.close();
        return event;
    }

    @Override
    public void saveEvent(Event event) {
        Session session= sessionFactory.openSession();
        session.persist(event);
        session.close();
    }

    @Override
    public List<Event> getEventByLocation(String location) {
        Session session= sessionFactory.openSession();
        Query<Event> eventQuery = session.createQuery("FROM Event et WHERE et.eventLocation.locationName = :location AND et.isPrivate = false", Event.class);
        List<Event> events= eventQuery.getResultList();

        session.close();
        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        Session session = sessionFactory.openSession();
        Query<Event> query= session.createQuery("FROM Event er WHERE er.eventType.title =:type ", Event.class);
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
    public List<Event> getEventByFilter(String location, LocalTime eventTime, LocalDate eventDate, EventType eventType) {
        Session session= sessionFactory.openSession();
        String query = "FROM Event er WHERE er.eventLocation.locationName=: location AND er.eventDate.eventStartTime = :eventTime AND er.eventDate.eventStartDate = :eventDate AND er.eventType =: eventType AND er.isPrivate =: false";
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

        Query<Event> eventQuery= session.createQuery("FROM  Event er WHERE er.eventDate.eventStartDate>= :date AND er.isPrivate =false ORDER BY er.eventTicket.ticketSold DESC", Event.class);
        eventQuery.setParameter("date", date);
        List<Event> events= eventQuery.getResultList();
        session.close();
        return events;
    }

    @Override
    public boolean existsByName(String name) {
        Session session= sessionFactory.openSession();

        Query<Boolean> exitsByUsername = session.createQuery("SELECT CASE WHEN count (er)> 0 THEN true ELSE false END FROM Event er WHERE er.name = :name", Boolean.class);
        exitsByUsername.setParameter("name", name);

        return exitsByUsername.uniqueResult();
    }


}
