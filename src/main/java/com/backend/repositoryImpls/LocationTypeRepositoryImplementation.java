package com.backend.repositoryImpls;

import com.backend.models.LocationType;
import com.backend.repositories.LocationTypeRepository;
import jdk.jshell.spi.ExecutionControl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LocationTypeRepositoryImplementation implements LocationTypeRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Optional<LocationType> getLocationTypeByName(String name) {
        Session session= sessionFactory.openSession();
        Query<LocationType> query =session.createQuery("FROM  LocationType  lt WHERE lt.locationTypeTitle =: name ", LocationType.class);
        query.setParameter("name", name);

        Optional<LocationType> locationType= query.uniqueResultOptional();

        session.close();

        return locationType;

    }
}
