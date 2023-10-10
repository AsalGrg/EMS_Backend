package com.backend.services;

import com.backend.dtos.AddEventDto;
import com.backend.models.Event;

import java.util.List;

public interface EventService{

    public Event getEventById(int id);

    public List<Event> getEventByPlace(String place);

    public List<Event> getEventByType(String type);

    public List<Event> getAllEvents();

    public Event addEvent(AddEventDto addEventDto);
}
