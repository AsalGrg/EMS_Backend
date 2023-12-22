package com.backend.serviceImpls;

import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.TicketType;
import com.backend.repositories.TicketTypeRepository;
import com.backend.services.TicketTypeService;
import org.springframework.stereotype.Service;

@Service
public class TicketTypeServiceImplementation implements TicketTypeService {

    private TicketTypeRepository ticketTypeRepository;

    public TicketTypeServiceImplementation(
           TicketTypeRepository ticketTypeRepository
    ){
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public TicketType findTicketTypeByName(String name) {
        return ticketTypeRepository.getTicketTypeByName(name).
                orElseThrow(()-> new ResourceNotFoundException("Invalid ticket type"));
    }
}
