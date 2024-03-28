package com.backend.repositoryImpls;

import com.backend.models.EventTicket;
import com.backend.repositories.EventTicketRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventTicketRepoImpl implements EventTicketRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventTicket saveEventTicket(EventTicket eventTicket) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventTicket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventTicket;
    }
}
