package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.AddStarringDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventDateDetailsDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.dtos.addEvent.EventTicketDetailsDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;
import org.springframework.web.multipart.MultipartFile;

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

     Event saveEvent(Event event);

     Event addEvent(AddEventRequestDto addEventDto, EventTicketDetailsDto eventTicketDetails, EventDateDetailsDto eventDateDetails);

}
