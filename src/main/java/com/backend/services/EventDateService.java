package com.backend.services;

import com.backend.dtos.addEvent.EventDateDetailsDto;
import com.backend.models.EventDate;

public interface EventDateService {

    EventDate saveEventDate(EventDateDetailsDto eventDateDetailsDto);
}
