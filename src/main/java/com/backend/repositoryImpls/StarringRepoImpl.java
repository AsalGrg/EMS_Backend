package com.backend.repositoryImpls;

import com.backend.models.EventStarring;
import com.backend.repositories.StarringRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StarringRepoImpl implements StarringRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveStarring(EventStarring eventStarring) {
        Session session = sessionFactory.openSession();
        session.persist(eventStarring);
        session.close();
    }
}
