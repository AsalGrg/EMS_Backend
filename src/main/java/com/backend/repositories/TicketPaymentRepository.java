package com.backend.repositories;

import com.backend.models.TicketPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPaymentRepository extends JpaRepository<TicketPayment , Integer> {
}
