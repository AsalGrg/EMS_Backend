package com.backend.repositories;

import com.backend.models.TicketType;

import java.util.Optional;

public interface TicketTypeRepository {

    Optional<TicketType> getTicketTypeByName(String name);
}
