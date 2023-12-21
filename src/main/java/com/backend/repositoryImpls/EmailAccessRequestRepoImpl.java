package com.backend.repositoryImpls;

import com.backend.models.Event;
import com.backend.models.EventAccessRequest;
import com.backend.models.User;
import com.backend.repositories.EventAccessRequestRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmailAccessRequestRepoImpl implements EventAccessRequestRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void saveEventAccessRequest(EventAccessRequest eventAccessRequest) {
        Session session= sessionFactory.openSession();
        session.persist(eventAccessRequest);
        session.close();
    }

    @Transactional
    @Override
    public Optional<EventAccessRequest> getByUserAndEvent(User user, Event event) {
        Session session= sessionFactory.openSession();
        Query<EventAccessRequest> query= session.createQuery("FROM EventAccessRequest er WHERE er.user =:user AND er.event =: event", EventAccessRequest.class);
        query.setParameter("event", event);
        query.setParameter("user", user);
        Optional<EventAccessRequest> eventAccessRequest= query.uniqueResultOptional();
        session.close();
        return eventAccessRequest;
    }

    @Transactional
    @Override
    public List<EventAccessRequest> findEventAccessRequestByEventOrganizer(String eventOrganizer) {
        Session session= sessionFactory.openSession();

        Query<EventAccessRequest> query = session.createQuery("FROM EventAccessRequest er WHERE er.event.eventOrganizer.username= :organizerName",
                EventAccessRequest.class);
        query.setParameter("organizerName", eventOrganizer);

        return query.getResultList();
    }
}
