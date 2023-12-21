package com.backend.repositoryImpls;

import com.backend.models.EmailVerification;
import com.backend.repositories.EmailVerificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmailVerificationRepoImpl implements EmailVerificationRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Optional<EmailVerification> getByEmailVerificationToken(String token) {
        Session session= entityManager.unwrap(Session.class);

        Query<EmailVerification> query= session.createQuery("FROM EmailVerification ev WHERE ev.verificationToken = :token", EmailVerification.class);
        query.setParameter("token", token);

        EmailVerification emailVerification= query.uniqueResult();
        return Optional.ofNullable(emailVerification);
    }

    @Transactional
    @Override
    public void saveEmailVerification(EmailVerification emailVerification) {
        Session session= entityManager.unwrap(Session.class);
        session.persist(emailVerification);
    }


}
