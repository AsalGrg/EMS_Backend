package com.backend.repositoryImpls;

import com.backend.models.CollectionEvents;
import com.backend.models.Event;
import com.backend.models.EventCollection;
import com.backend.repositories.EventCollectionRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class EventCollectionRepositoryImpl implements EventCollectionRepository {

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public EventCollection saveEventCollection(EventCollection eventCollection) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

// Using merge to save or update the entity
        EventCollection savedCollection = session.merge(eventCollection);

        tx.commit();
        session.close();

        return savedCollection;
    }

    @Override
    public List<EventCollection> getAllEventCollection() {
        Session session= sessionFactory.openSession();
        Query<EventCollection> eventQuery = session.createQuery("FROM EventCollection ec WHERE ec.isShown=true ", EventCollection.class);
        List<EventCollection> collections= eventQuery.getResultList();
        session.close();
        return collections;
    }

    @Override
    public List<Event> getAllEventCollectionEvents(int collectionId) {
        Session session= sessionFactory.openSession();
        Query<Event> eventQuery = session.createQuery("SELECT ce.event  FROM CollectionEvents ce JOIN EventCollection  ec on ce.collection.id= ec.id WHERE ec.id=:id ", Event.class);
        eventQuery.setParameter("id", collectionId);
        List<Event> events= eventQuery.getResultList();
        session.close();
        return events;
    }

    @Override
    public List<EventCollection> getUpcomingEventsOfCollection(int eventCollectionId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM CollectionEvents ce " +
                    "WHERE ce.id = :id " +
                    "AND ce.event.eventFirstPageDetails.eventDate.eventStartDate > CURRENT_DATE";

            Query<EventCollection> eventQuery = session.createQuery(hql, EventCollection.class);
            eventQuery.setParameter("id", eventCollectionId);

            return eventQuery.getResultList();
        } catch (HibernateException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return Collections.emptyList(); // or throw an exception
        }
    }



    @Override
    public EventCollection getEventCollectionById(int collectionId) {
        Session session= sessionFactory.openSession();
        Query<EventCollection> eventQuery = session.createQuery("FROM EventCollection ec WHERE ec.id=:id ", EventCollection.class);
        eventQuery.setParameter("id", collectionId);
        EventCollection eventCollection= eventQuery.uniqueResult();
        session.close();
        return eventCollection;
    }
}
