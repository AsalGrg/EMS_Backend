package com.backend.repositories;

import com.backend.models.Event;
import com.backend.models.TicketPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketPaymentRepository extends JpaRepository<TicketPayment , Integer> {

    List<TicketPayment> findTicketPaymentByEvent(Event event);
    List<TicketPayment> findTicketPaymentByEvent_EventOrganizer_Username(String vendorName);
}
