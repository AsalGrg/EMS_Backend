package com.backend.repositories;

import com.backend.models.Event;
import com.backend.models.PromoCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocodeRepository extends JpaRepository<PromoCode, Integer> {

    boolean existsByTitle(String title);
    Optional<PromoCode> findById(int id);
    Optional<PromoCode> findByTitle(String title);
    PromoCode findByEventAndTitle(Event event, String title);
    List<PromoCode> findByEvent(Event event);
    @Transactional
    @Modifying
    @Query("UPDATE PromoCode p SET p.isActive = :isActive WHERE p.id = :promoCodeId")
    void updateActiveStatusById(Integer promoCodeId, boolean isActive);
}
