package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;

import java.util.List;

public interface EventService{

    public Event getEventById(int id);

    public List<Event> getEventByPlace(String place);

    public List<Event> getEventByType(String type);

    public List<Event> getAllEvents();

    public EventResponseDto addEvent(AddEventRequestDto addEventDto);

    public EventResponseDto getEventByAccessToken(String accessToken, String username);

    public PromoCode addPromocode(AddPromoCodeDto promoCodeDto);

    //handler to get the eventAccessRequests
    public List<EventAccessRequestsView> getEventAccessRequests(String username);
}
