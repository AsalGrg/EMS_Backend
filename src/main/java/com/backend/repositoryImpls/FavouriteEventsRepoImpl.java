package com.backend.repositoryImpls;

import com.backend.models.FavouriteEvents;
import com.backend.models.VendorFollowers;
import com.backend.repositories.FavouriteEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class FavouriteEventsRepoImpl implements FavouriteEventsRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addFavouriteEvent(FavouriteEvents favouriteEvent) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

// Using merge to save or update the entity
        session.merge(favouriteEvent);

        tx.commit();
        session.close();
    }

    @Override
    public void removeFavouriteEvent(int userId, int eventId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Construct and execute Hibernate query to remove follower

            Query<?> query = session.createQuery("DELETE FROM FavouriteEvents fe WHERE fe.user.id = :userId AND fe.event.id=: eventId");
            query.setParameter("userId", userId);
            query.setParameter("eventId", eventId);

            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Close the Hibernate session
            session.close();
        }
    }

    @Override
    public List<FavouriteEvents> getFavouriteEventsOfUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<FavouriteEvents> query = session.createQuery("FROM FavouriteEvents vf WHERE vf.user.id=:userId", FavouriteEvents.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            return null; // Return -1 to indicate failure
        }
    }

    @Override
    public FavouriteEvents getFavouriteEventByBothId(int userId, int eventId) {
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<FavouriteEvents> query = session.createQuery("FROM FavouriteEvents fe WHERE fe.user.id=: userId AND fe.event.id =:eventId", FavouriteEvents.class);
            query.setParameter("userId", userId);
            query.setParameter("eventId", eventId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return -1 to indicate failure
        }
    }
}
