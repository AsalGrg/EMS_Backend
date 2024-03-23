package com.backend.repositoryImpls;

import com.backend.models.EventDate;
import com.backend.repositories.EventDateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDateRepoImpl implements EventDateRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventDate saveEventDate(EventDate eventDate) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventDate);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventDate;
    }

}
