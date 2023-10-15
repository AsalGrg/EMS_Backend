package com.backend.repositories;

import com.backend.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocodeRepository extends JpaRepository<PromoCode, Integer> {

    boolean existsByName(String name);
}
