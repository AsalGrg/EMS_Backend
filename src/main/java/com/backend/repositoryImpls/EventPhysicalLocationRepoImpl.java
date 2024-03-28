package com.backend.repositoryImpls;

import com.backend.dtos.addEvent.EventPhysicalLocationDetailsDto;
import com.backend.models.EventLocation;
import com.backend.models.EventPhysicalLocationDetails;
import com.backend.models.EventStarring;
import com.backend.repositories.EventPhysicalLocationRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EventPhysicalLocationRepoImpl implements EventPhysicalLocationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EventPhysicalLocationDetails getEventPhysicalLocationByEventLocation(EventLocation eventLocation) {
        Session session = sessionFactory.openSession();
        Query<EventPhysicalLocationDetails> eventPhysicalLocationDetailsQuery= session.createQuery("FROM event_physical_location_details epd WHERE epd.eventLocation.id = :eventLocationId", EventPhysicalLocationDetails.class);
        eventPhysicalLocationDetailsQuery.setParameter("eventLocationId", eventLocation.getId());
        return eventPhysicalLocationDetailsQuery.uniqueResult();
    }

    @Override
    public void savePhysicalLocationDetails(EventPhysicalLocationDetails eventPhysicalLocationDetails) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.merge(eventPhysicalLocationDetails);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace(); // Handle the exception appropriately
            } finally {
                session.close();
            }
    }
}
