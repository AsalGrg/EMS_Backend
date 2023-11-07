package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;

import java.util.List;

 public interface EventService{

    Event getEventById(int id);

    List<Event> getEventByPlace(String place);

     List<Event> getEventByType(String type);

     List<Event> getAllEvents();

     Event getEventByName(String name);

     void saveEvent(Event event);

     EventResponseDto addEvent(AddEventRequestDto addEventDto);

     Event getEventByAccessToken(String accessToken);

     EventResponseDto enterEventByAccessToken(String accessToken, String username);

    //handler to get the eventAccessRequests
     List<EventAccessRequestsView> getEventAccessRequests(String username);

     void addEventVendorRequestAction(String username, String action, String eventName);

     void makeEventAccessRequest(String username, String accessToken);
}
