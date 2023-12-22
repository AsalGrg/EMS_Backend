package com.backend.services;

import com.backend.models.TicketType;

public interface TicketTypeService {

    TicketType findTicketTypeByName (String name);
}
