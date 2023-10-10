package com.backend.serviceImpls;

import com.backend.dtos.AddEventDto;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.repositories.EventRepository;
import com.backend.repositories.EventTypeRepository;
import com.backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EventServiceImplementation implements EventService {

    private EventRepository eventRepository;

    private EventTypeRepository eventTypeRepository;

    @Autowired
    public EventServiceImplementation(EventRepository eventRepository, EventTypeRepository eventTypeRepository){
        this.eventRepository= eventRepository;
        this.eventTypeRepository= eventTypeRepository;
    }

    @Override
    public Event getEventById(int id) {
        return this.eventRepository.findEventById(id).get();
    }

    @Override
    public List<Event> getEventByPlace(String place) {
        List<Event> events= this.eventRepository.findEventByLocation(place);

        if (events.isEmpty()){
            throw new ResourceNotFoundException("Events for the given place are currently not available");
        }

        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        return this.eventRepository.findEventByType(type);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = this.eventRepository.findAll();

        if(events.isEmpty()){
            throw new ResourceNotFoundException("Currently no available events");
        }

        return events;
    }

    @Override
    public Event addEvent(AddEventDto addEventDto) {

        boolean eventNameExists= this.eventRepository.existsByName(addEventDto.getName());

        if(eventNameExists){
            throw new ResourceAlreadyExistsException("Event name already exists");
        }

        Event event= new Event();
        event.setName(addEventDto.getName());
        event.setLocation(addEventDto.getLocation());
        event.setPublished_date(addEventDto.getEventDate());
        event.setPublished_date(addEventDto.getPublishedDate());
        event.setEvent_date(addEventDto.getEventDate());
        event.setDescription(addEventDto.getDescription());
        event.setPrivate(addEventDto.isPrivate());
        event.setEntryFee(addEventDto.getEntryFee());
        event.setSeats(addEventDto.getSeats());

        //other entities such as event group are to be set
        event.setEventType(this.eventTypeRepository.getEventTypeByTitle(addEventDto.getEventType()));


        //adding the event in the database
        return this.eventRepository.save(event);
    }

}
