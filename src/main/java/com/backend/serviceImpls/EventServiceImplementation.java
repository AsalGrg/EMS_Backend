package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.AddStarringDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.*;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.*;
import com.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;


@Service
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;

    //must be replaced wit eventCategoryService
    private EventCategoryService eventCategoryService;

    private final UserService userService;

    private final PromoCodeService promoCodeService;

    private final VendorCredentialService vendorCredentialsService;

    private final RoleService roleService;

    private EventLocationService eventLocationService;

    private EventDateService eventDateService;

    private EventTicketService eventTicketService;

    private EventVisibilityService eventVisibilityService;

    private  StarringService starringService;

    private EventPhysicalLocationDetailsService eventPhysicalLocationDetailsService;

    private final CloudinaryUploadService cloudinaryUploadService;


    @Autowired
    public EventServiceImplementation

            (EventRepository eventRepository, UserService userService
            , PromoCodeService promoCodeService,VendorCredentialService vendorCredentialsService,
             EventLocationService eventLocationService, EventDateService eventDateService,
             EventTicketService eventTicketService, EventVisibilityService eventVisibilityService,
             EventCategoryService eventCategoryService,
             StarringService starringService,RoleService roleService, CloudinaryUploadService cloudinaryUploadServiceImpl,
             EventPhysicalLocationDetailsService eventPhysicalLocationDetailsService){

        this.eventRepository= eventRepository;
        this.userService= userService;
        this.promoCodeService= promoCodeService;
        this.vendorCredentialsService= vendorCredentialsService;
        this.roleService= roleService;
        this.eventLocationService= eventLocationService;
        this.eventDateService= eventDateService;
        this.eventTicketService= eventTicketService;
        this.eventVisibilityService = eventVisibilityService;
        this.starringService =starringService;
        this.eventCategoryService = eventCategoryService;
        this.cloudinaryUploadService= cloudinaryUploadServiceImpl;
        this.eventPhysicalLocationDetailsService= eventPhysicalLocationDetailsService;
    }

    public EventResponseDto changeToEventDto(Event event, EventPhysicalLocationDetails physicalLocationDetails){
//        return new EventResponseDto(event.getName(), event.getEventDate().;, event.getPublished_date(), event.getEntryFee(),event.getEventType().getTitle());
        if(physicalLocationDetails==null) {
            return EventResponseDto.builder().
                    eventName(event.getName())
                    .startDate(event.getEventDate().getEventStartDate())
                    .endDate(event.getEventDate().getEventEndDate())
                    .category(event.getEventCategory().getTitle())
                    .ticketType(event.getEventTicket().getTicketType().getTitle())
                    .ticketPrice(event.getEventTicket().getTicketPrice())
                    .build();
        }
        return EventResponseDto.builder().
                eventName(event.getName())
                .startDate(event.getEventDate().getEventStartDate())
                .endDate(event.getEventDate().getEventEndDate())
                .category(event.getEventCategory().getTitle())
                .ticketType(event.getEventTicket().getTicketType().getTitle())
                .ticketPrice(event.getEventTicket().getTicketPrice())
                .country(physicalLocationDetails.getCountry())
                .location_display_name(physicalLocationDetails.getDisplayName())
                .build();
    }

    @Override
    public Event getEventById(int id) {
        Event event=  eventRepository.getEventById(id);
        if(event==null){
            throw new ResourceNotFoundException("Event with the given id does not exist");
        }

        return event;
    }


    @Override
    public Event getEventByName(String name){
        return eventRepository.getEventByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid event name '"+name+"'"));
    }


    @Override
    public Event saveEvent(Event event){
        return eventRepository.saveEvent(event);
    }

    @Override
    public List<Event> getEventByPlace(String place) {
        List<Event> events= eventRepository.getEventByLocation(place);

        if (events.isEmpty()){
            throw new ResourceNotFoundException("Events for the given place are currently not available");
        }
        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        return eventRepository.getEventByType(type);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.getAllEvents();

        if(events.isEmpty()){
            throw new ResourceNotFoundException("Currently no available events");
        }
        return events;
    }

    //service method to get all the events from the filter such as time, date and place and venue
    @Override
    public List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto){
        List<Event> filterEventsList = eventRepository.getEventByFilter(
                searchEventByFilterDto.getLocation(),
                searchEventByFilterDto.getEvent_time(),
                searchEventByFilterDto.getEvent_date(),
                eventCategoryService.getEventCategoryByName(searchEventByFilterDto.getEvent_category())
        );

        if(filterEventsList.isEmpty()) throw new ResourceNotFoundException("Searched event not found at the moment, you may like other events");

        List<EventResponseDto> filteredEventsView = new ArrayList<>();

        for(Event event: filterEventsList){
            filteredEventsView.add(changeToEventDto(event, null));
        }

        return filteredEventsView;
    }

    @Override
    public List<EventResponseDto> getEventsBySearch(String eventTitle, String eventVenue) {

        if(eventVenue.equals("online")){

            List<Event> onlineEvents = getOnlineEventsByTitle(eventTitle);
            List<EventResponseDto> onlineEventsResponse = new ArrayList<>();
            if(onlineEvents!=null){
                for (Event each : onlineEvents){
                    onlineEventsResponse.add(changeToEventDto(each, null));
                }
            }

            return onlineEventsResponse;
        }

        List<Event> physicalEvents = eventRepository.getPhysicalEvents(eventTitle, eventVenue);
        List<EventResponseDto> physicalEventsResponse= new ArrayList<>();

        if(physicalEvents!=null){
            for(Event each: physicalEvents){
                physicalEventsResponse.add(changeToEventDto(each,getEventPhysicalLocationDetails(each.getEventLocation())));
            }
        }
        return physicalEventsResponse;
    }

    private EventPhysicalLocationDetails getEventPhysicalLocationDetails(EventLocation eventLocation){
        return  eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(eventLocation);
    }
    @Override
    public List<Event> getOnlineEventsByTitle(String eventTitle) {

        List<Event> onlineEvents = new ArrayList<>();

        eventRepository.getOnlineEvents(eventTitle);
        return null;
    }

    //service handler method to get the trending events
    @Override
    public List<EventResponseDto> getTrendingEvents(){
        List<Event> allTrendingEvents= eventRepository.getTrendingEvents(LocalDate.now());

        if(allTrendingEvents.isEmpty()) throw new ResourceNotFoundException("No events at the moment!");
        List<EventResponseDto> allTrendingEventsView= new ArrayList<>();

        for(Event each: allTrendingEvents){
            allTrendingEventsView.add(changeToEventDto(each,null));
        }

        return allTrendingEventsView;
    }



    //service method for adding new event
    @Override
    public Event addEvent(
            AddEventRequestDto addEventDto, EventTicketDetailsDto eventTicketDetails, EventDateDetailsDto eventDateDetails,
            EventStarringDetails eventStarringDetails, EventPhysicalLocationDetailsDto eventPhysicalLocationDetailsDto) {

        boolean eventNameExists = eventRepository.existsByName(addEventDto.getEventName());

        if(eventNameExists) throw new ResourceAlreadyExistsException("Event name already exists");

        //saving the event location detail
        EventLocation eventLocation = eventLocationService.saveEventLocation(addEventDto.getLocationType(), addEventDto.getLocationName());

        //saving the event date details in the event date table
        EventDate eventDate = eventDateService.saveEventDate(eventDateDetails);

        String coverImageUrl = cloudinaryUploadService.uploadImage(addEventDto.getEventCoverPhoto(), "Event Cover Photo");

        EventTicket eventTicket= eventTicketService.saveEventTicket(eventTicketDetails);

        EventVisibility eventVisibility = eventVisibilityService.saveEventVisibility(
                addEventDto.getEventVisibilityType(), addEventDto.getEventAccessPassword()
        );

        //getting the current user userName
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String username= "Check User";//static for now
        User organizer = userService.getUserByUsername(username);

        EventCategory eventCategory = eventCategoryService.getEventCategoryByName(addEventDto.getEventCategory());
        boolean hasStarring= addEventDto.isHasStarring();

        Event savedEvent = saveEvent(
                Event
                        .builder()
                        .name(addEventDto.getEventName())
                        .eventLocation(eventLocation)
                        .eventDate(eventDate)
                        .eventCoverPage(coverImageUrl)
                        .hasStarring(hasStarring)
                        .aboutEvent(addEventDto.getAboutEvent())
                        .isPrivate(addEventDto.isPrivate())
                        .eventTicket(eventTicket)
                        .eventVisibility(eventVisibility)
                        .eventOrganizer(organizer)
                        .eventCategory(eventCategory)
                        .build()
        );

        if(hasStarring){
            starringService.saveStarring(eventStarringDetails, savedEvent);
        }
        if(eventLocation.getLocationType().getLocationTypeTitle().equals("venue")){
            eventPhysicalLocationDetailsService.savePhysicalLocationDetails(eventPhysicalLocationDetailsDto, eventLocation);
        }


        return null;
    }

    @Override
    public void addPromoCode(AddPromoCodeDto promoCodeDto){
        promoCodeService.addPromocode(promoCodeDto, getEventByName(promoCodeDto.getName()));
    }

}
