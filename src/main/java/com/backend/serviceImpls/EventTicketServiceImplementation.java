package com.backend.serviceImpls;

import com.backend.dtos.addEvent.EventTicketDetailsDto;
import com.backend.models.EventTicket;
import com.backend.repositories.EventTicketRepository;
import com.backend.services.EventTicketService;
import com.backend.services.TicketTypeService;
import org.springframework.stereotype.Service;

@Service
public class EventTicketServiceImplementation implements EventTicketService {

    private EventTicketRepository eventTicketRepository;

    private TicketTypeService ticketTypeService;

    public EventTicketServiceImplementation(
            EventTicketRepository eventTicketRepository,
            TicketTypeService ticketTypeService
    ){
        this.eventTicketRepository= eventTicketRepository;
        this.ticketTypeService= ticketTypeService;
    }

    @Override
    public EventTicket saveEventTicket(EventTicketDetailsDto eventTicketDetailsDto) {
        return eventTicketRepository.saveEventTicket(
                EventTicket
                        .builder()
                        .ticketType(ticketTypeService.findTicketTypeByName(eventTicketDetailsDto.getTicketType()))
                        .ticketName(eventTicketDetailsDto.getTicketName())
                        .ticketPrice(eventTicketDetailsDto.getTicketPrice())
                        .ticketQuantity(eventTicketDetailsDto.getTicketQuantity())
                        .ticketStartDate(eventTicketDetailsDto.getSaleStartDate())
                        .ticketStartTime(eventTicketDetailsDto.getSaleStartTime())
                        .ticketEndDate(eventTicketDetailsDto.getSaleEndDate())
                        .ticketEndTime(eventTicketDetailsDto.getSaleEndTime())
                        .ticketSold(0)
                        .build()
        );
    }
}
