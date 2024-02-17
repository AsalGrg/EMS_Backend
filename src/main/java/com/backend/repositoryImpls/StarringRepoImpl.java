package com.backend.repositoryImpls;

import com.backend.models.EventStarring;
import com.backend.repositories.StarringRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class StarringRepoImpl implements StarringRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveStarring(EventStarring eventStarring) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(eventStarring);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            session.close();
        }
        log.info("idd"+ eventStarring.getId());
    }

}
