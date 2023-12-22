package com.backend.repositoryImpls;

import com.backend.models.EventVisibility;
import com.backend.repositories.EventVisibilityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventVisibilityRepoImpl implements EventVisibilityRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventVisibility saveEventVisibility(EventVisibility eventVisibility) {
        Session session =sessionFactory.openSession();
        return (EventVisibility) session.save(eventVisibility);
    }
}
