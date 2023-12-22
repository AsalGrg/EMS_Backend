package com.backend.repositoryImpls;

import com.backend.models.EventDate;
import com.backend.repositories.EventDateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDateRepoImpl implements EventDateRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventDate saveEventDate(EventDate eventDate) {
        Session session= sessionFactory.openSession();
        return (EventDate) session.save(eventDate);
    }
}
