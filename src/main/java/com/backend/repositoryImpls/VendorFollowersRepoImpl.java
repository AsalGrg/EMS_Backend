package com.backend.repositoryImpls;

import com.backend.models.VendorFollowers;
import com.backend.repositories.VendorFollowersRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class VendorFollowersRepoImpl implements VendorFollowersRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public VendorFollowers getVendorByFollowerIdAndFollowingId(int followedTo, int followedBy) {
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<VendorFollowers> query = session.createQuery("FROM VendorFollowers vf WHERE vf.followedTo.id=:followedTo AND vf.followedBy.id=:followedBy", VendorFollowers.class);
            query.setParameter("followedTo", followedTo);
            query.setParameter("followedBy", followedBy);
            return query.getSingleResultOrNull();
        } catch (Exception e) {
            return null; // Return -1 to indicate failure
        }
    }

    @Override
    public void addFollowers(VendorFollowers vendorFollower) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

// Using merge to save or update the entity
        session.merge(vendorFollower);

        tx.commit();
        session.close();
    }

    @Override
    public void removeFollower(int followedBy, int followedTo) {
        // Open a Hibernate session
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Construct and execute Hibernate query to remove follower

            log.info("Followed by: "+ followedBy);
            log.info("Followed to: "+ followedTo);
            Query<?> query = session.createQuery("DELETE FROM VendorFollowers vf WHERE vf.followedBy.id = :followedBy AND vf.followedTo.id=: followedTo");
            query.setParameter("followedBy", followedBy);
            query.setParameter("followedTo", followedTo);

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
    public int getNoOfFollowers(int userId) {
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM VendorFollowers vf WHERE vf.followedTo.id=:userId", Long.class);
            query.setParameter("userId", userId);
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }


    @Override
    public int getNoOfFollowing(int userId) {

        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM VendorFollowers vf WHERE vf.followedBy.id=:userId", Long.class);
            query.setParameter("userId", userId);
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }

    @Override
    public VendorFollowers getVendorFollowerDetailByBothId(int vendorId, int followerId){
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<VendorFollowers> query = session.createQuery("FROM VendorFollowers vf WHERE vf.followedTo.id=: vendorId AND vf.followedBy.id=:followerId", VendorFollowers.class);
            query.setParameter("vendorId", vendorId);
            query.setParameter("followerId", followerId);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return -1 to indicate failure
        }
    }

    @Override
    public List<VendorFollowers> getAllFollowings(int userId) {
        try (Session session = sessionFactory.openSession()) {
            // Construct and execute Hibernate query to get the count of followers
            Query<VendorFollowers> query = session.createQuery("FROM VendorFollowers vf WHERE vf.followedBy.id=:userId", VendorFollowers.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return -1 to indicate failure
        }
    }
}
