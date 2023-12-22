package com.backend.repositoryImpls;

import com.backend.models.TicketType;
import com.backend.repositories.TicketTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TicketTypeRepoImpl implements TicketTypeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<TicketType> getTicketTypeByName(String name) {
        Session session= sessionFactory.openSession();
        Query<TicketType> ticketTypeQuery = session.createQuery("FROM TicketType tt WHERE tt.title =:name", TicketType.class);
        ticketTypeQuery.setParameter("name", name);

        Optional<TicketType> ticketType= ticketTypeQuery.uniqueResultOptional();
        session.close();

        return ticketType;
    }

}
