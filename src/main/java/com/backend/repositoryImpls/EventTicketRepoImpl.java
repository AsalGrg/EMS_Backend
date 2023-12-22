package com.backend.repositoryImpls;

import com.backend.models.EventTicket;
import com.backend.repositories.EventTicketRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventTicketRepoImpl implements EventTicketRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventTicket saveEventTicket(EventTicket eventTicket) {
        Session session= sessionFactory.openSession();
        return (EventTicket)session.save(eventTicket);
    }
}
