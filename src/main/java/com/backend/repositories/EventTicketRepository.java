package com.backend.repositories;

import com.backend.models.EventTicket;

public interface EventTicketRepository {

    EventTicket saveEventTicket(EventTicket eventTicket);
}
