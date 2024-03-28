package com.backend.services;

import com.backend.dtos.addEvent.EventTicketDetailsDto;
import com.backend.models.EventTicket;

public interface EventTicketService {

    EventTicket saveEventTicket(EventTicketDetailsDto eventTicketDetailsDto);
    EventTicket updateEventTicket(EventTicketDetailsDto eventTicketDetailsDto, int eventTicketId);
}
