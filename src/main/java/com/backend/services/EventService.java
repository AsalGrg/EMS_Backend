package com.backend.services;

import com.backend.dtos.*;
import com.backend.dtos.aboutEvent.EventDescriptionResponseDto;
import com.backend.dtos.addEvent.*;
import com.backend.dtos.draftDtos.EventDraftDetails;
import com.backend.dtos.vendor.EventInternalDetailsDto;
import com.backend.models.Event;
import com.backend.models.EventCollection;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

 public interface EventService{

     void likeEvent(int eventId);
    Event getEventById(int id);
    List<EventResponseDto> getAllVendorEventsSnippets();
    EventInternalDetailsDto getEventInternalDetails(int eventId);
    List<OrderDetailsDto> getAllVendorOrders();
    ApplyPromoCodeResponseDto isPromoCodeValid(String promoCode , int eventId, double totalAmount);
    EventDescriptionResponseDto getAboutEventByEventId(int eventId, HttpServletRequest request, EventAccessDetails eventAccessDetails) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    List<EventResponseDto>  getAllUserLikedEvents();
    List<EventResponseDto> getQuickSearchResult( String keyword);
    List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto);
     List<EventResponseDto> getEventsBySearch(String eventTitle, String eventVenue);

     List<Event> getOnlineEventsByTitle(String eventTitle);

     List<EventResponseDto> getTrendingEvents();

     void addPromoCode(AddPromoCodeDto promoCodeDto);

     void activePromoCode(int promoCodeId);

     void deactivePromoCode(int promoCodeId);

    EventAndVendorsByLocationDto getEventByPlace(String place);

     CategoryDetailsDto getEventByTypeAndLocation(String type, String location, HttpServletRequest request);

     List<Event> getAllEvents();

     Event getEventByName(String name);

     Event saveEvent(Event event);

     Event addEvent(AddEventRequestDto addEventDto, EventTicketDetailsDto eventTicketDetails, EventDateDetailsDto eventDateDetails, EventStarringDetails eventStarringDetails, EventPhysicalLocationDetailsDto eventPhysicalLocationDetailsDto);

     Integer addFirstPageDetails(AddEventFirstPageDto addEventFirstPageDto, EventDateDetailsDto eventDateDetailsDto, EventPhysicalLocationDetailsDto eventPhysicalLocationDetails);

     void addEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails);

     void addEventThirdPageDetails(EventTicketDetailsDto eventTicketDetailsDto);

     void addEventFourthPageDetails (AddEventFourthPageDto addEventFourthPageDto);
     EventDraftDetails getEventDraftDetails(int eventId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
     EventDraftDetails getEventEditDetails(int eventId)  throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
     void addEventCollection(EventCollectionSnippet eventCollectionSnippet);
     List<EventCollectionSnippet> getAllEventCollections();
     List<EventResponseDto> getAllEventRequests();
     void updateEventRequest(int eventId, String action); //can be accept or reject;
 }
