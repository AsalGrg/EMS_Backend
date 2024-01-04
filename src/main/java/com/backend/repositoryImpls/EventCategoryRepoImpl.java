package com.backend.repositoryImpls;

import com.backend.models.EventCategory;
import com.backend.repositories.EventCategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EventCategoryRepoImpl implements EventCategoryRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Optional<EventCategory> getEventCategoryByName(String name) {
        Session session= sessionFactory.openSession();
        Query<EventCategory> eventCategoryQuery= session.createQuery("FROM EventCategory ec WHERE ec.title =:name",EventCategory.class);
        eventCategoryQuery.setParameter("name", name);
        Optional<EventCategory> eventCategory = eventCategoryQuery.uniqueResultOptional();
        session.close();
        return eventCategory;
    }
}
