package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.aboutEvent.EventDescriptionResponseDto;
import com.backend.dtos.addEvent.*;
import com.backend.models.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

 public interface EventService{

    Event getEventById(int id);
    EventDescriptionResponseDto getAboutEventByEventId(int eventId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    List<EventResponseDto> getQuickSearchResult( String keyword);
    List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto);

     List<EventResponseDto> getEventsBySearch(String eventTitle, String eventVenue);

     List<Event> getOnlineEventsByTitle(String eventTitle);

     List<EventResponseDto> getTrendingEvents();

     void addPromoCode(AddPromoCodeDto promoCodeDto);

    List<EventResponseDto> getEventByPlace(String place);

     List<Event> getEventByType(String type);

     List<Event> getAllEvents();

     Event getEventByName(String name);

     Event saveEvent(Event event);

     Event addEvent(AddEventRequestDto addEventDto, EventTicketDetailsDto eventTicketDetails, EventDateDetailsDto eventDateDetails, EventStarringDetails eventStarringDetails, EventPhysicalLocationDetailsDto eventPhysicalLocationDetailsDto);

     Integer addFirstPageDetails(AddEventFirstPageDto addEventFirstPageDto, EventDateDetailsDto eventDateDetailsDto, EventPhysicalLocationDetailsDto eventPhysicalLocationDetails);

     void addEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails);

     void addEventThirdPageDetails(EventTicketDetailsDto eventTicketDetailsDto);

     void addEventFourthPageDetails (AddEventFourthPageDto addEventFourthPageDto);

 }
