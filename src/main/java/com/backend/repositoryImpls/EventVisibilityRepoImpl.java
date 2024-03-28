package com.backend.repositoryImpls;

import com.backend.models.EventVisibility;
import com.backend.repositories.EventVisibilityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventVisibilityRepoImpl implements EventVisibilityRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventVisibility saveEventVisibility(EventVisibility eventVisibility) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(eventVisibility);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }

        return eventVisibility;
    }
}
