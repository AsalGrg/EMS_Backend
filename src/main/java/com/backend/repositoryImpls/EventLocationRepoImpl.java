package com.backend.repositoryImpls;


import com.backend.models.EventLocation;
import com.backend.repositories.EventLocationRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventLocationRepoImpl implements EventLocationRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public EventLocation saveEventLocation(EventLocation eventLocation) {
        Session session= sessionFactory.openSession();
        return (EventLocation) session.save(eventLocation);
    }
}
