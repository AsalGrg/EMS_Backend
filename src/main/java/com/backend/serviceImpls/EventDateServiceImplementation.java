package com.backend.serviceImpls;

import com.backend.dtos.addEvent.EventDateDetailsDto;
import com.backend.models.EventDate;
import com.backend.repositories.EventDateRepository;
import com.backend.services.EventDateService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventDateServiceImplementation implements EventDateService {

    private EventDateRepository eventDateRepository;

    public EventDateServiceImplementation(
            EventDateRepository eventDateRepository
    ){
        this.eventDateRepository= eventDateRepository;
    }

    @Override
    public EventDate saveEventDate(EventDate eventDate) {
        return eventDateRepository.saveEventDate(
                eventDate
        );
    }
}
