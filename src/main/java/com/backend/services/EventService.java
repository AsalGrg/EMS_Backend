package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;

import java.util.List;

 public interface EventService{

    Event getEventById(int id);

    List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto);

     List<EventResponseDto> getTrendingEvents();

     void addPromoCode(AddPromoCodeDto promoCodeDto);

    List<Event> getEventByPlace(String place);

     List<Event> getEventByType(String type);

     List<Event> getAllEvents();

     Event getEventByName(String name);

     void saveEvent(Event event);

     EventResponseDto addEvent(AddEventRequestDto addEventDto);

}
