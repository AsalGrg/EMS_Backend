package com.backend.repositoryImpls;

import com.backend.models.VisibilityType;
import com.backend.repositories.VisibilityTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VisibilityTypeRepoImpl implements VisibilityTypeRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Optional<VisibilityType> getVisibilityTypeByName(String name) {

        Session session= sessionFactory.openSession();
        Query<VisibilityType> visibilityTypeQuery = session.createQuery("FROM VisibilityType vt WHERE vt.title =: name", VisibilityType.class);
        visibilityTypeQuery.setParameter("name", name);
        Optional<VisibilityType> visibilityType = visibilityTypeQuery.uniqueResultOptional();

        session.close();
        return visibilityType;
    }
}
