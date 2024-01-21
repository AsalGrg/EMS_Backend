package com.backend.repositoryImpls;


import com.backend.models.EventLocation;
import com.backend.repositories.EventLocationRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventLocationRepoImpl implements EventLocationRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public EventLocation saveEventLocation(EventLocation eventLocation) {
            Session session = sessionFactory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.save(eventLocation);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace(); // Handle the exception appropriately
            } finally {
                session.close();
            }

            return eventLocation;
        }

}
