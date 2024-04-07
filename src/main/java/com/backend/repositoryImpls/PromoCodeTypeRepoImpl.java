package com.backend.repositoryImpls;

import com.backend.models.Event;
import com.backend.models.PromoCodeType;
import com.backend.repositories.PromoCodeTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PromoCodeTypeRepoImpl implements PromoCodeTypeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public PromoCodeType getPromoCodeTypeByTitle(String title) {
        Session session= sessionFactory.openSession();
        Query<PromoCodeType> query = session.createQuery("FROM PromoCodeType pt WHERE pt.title =: title", PromoCodeType.class);

        query.setParameter("title", title);
        PromoCodeType promoCodeType= query.getSingleResultOrNull();
        session.close();
        return  promoCodeType;
    }
}
