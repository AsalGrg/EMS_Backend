package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
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
    private final EventTypeService eventTypeService;

    private final UserService userService;

    private final PromoCodeService promoCodeService;

    private final VendorCredentialService vendorCredentialsService;

    private final RoleService roleService;


    private EventLocationService eventLocationService;

    private EventDateService eventDateService;

    private EventTicketService eventTicketService;

    private EventVisibilityService eventVisibilityService;

    private  StarringService starringService;

    private final CloudinaryUploadService cloudinaryUploadService;


    @Autowired
    public EventServiceImplementation

            (EventRepository eventRepository, EventTypeService eventTypeService, UserService userService
            , PromoCodeService promoCodeService,VendorCredentialService vendorCredentialsService,
             EventLocationService eventLocationService, EventDateService eventDateService,
             EventTicketService eventTicketService, EventVisibilityService eventVisibilityService,
             StarringService starringService,RoleService roleService, CloudinaryUploadService cloudinaryUploadServiceImpl){

        this.eventRepository= eventRepository;
        this.eventTypeService= eventTypeService;
        this.userService= userService;
        this.promoCodeService= promoCodeService;
        this.vendorCredentialsService= vendorCredentialsService;
        this.roleService= roleService;
        this.eventLocationService= eventLocationService;
        this.eventDateService= eventDateService;
        this.eventTicketService= eventTicketService;
        this.eventVisibilityService = eventVisibilityService;
        this.starringService =starringService;
        this.cloudinaryUploadService= cloudinaryUploadServiceImpl;
    }

    public EventResponseDto changeToEventDto(Event event){
//        return new EventResponseDto(event.getAccessToken(), event.getName(), event.getLocation(), event.getPublished_date(), event.getEntryFee(),event.getEventType().getTitle());
    return null;
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
                eventTypeService.getEventTypeByTitle(searchEventByFilterDto.getEvent_category())
        );

        if(filterEventsList.isEmpty()) throw new ResourceNotFoundException("Searched event not found at the moment, you may like other events");

        List<EventResponseDto> filteredEventsView = new ArrayList<>();

        for(Event event: filterEventsList){
            filteredEventsView.add(changeToEventDto(event));
        }

        return filteredEventsView;
    }

    //service handler method to get the trending events
    @Override
    public List<EventResponseDto> getTrendingEvents(){
        List<Event> allTrendingEvents= eventRepository.getTrendingEvents(LocalDate.now());

        if(allTrendingEvents.isEmpty()) throw new ResourceNotFoundException("No events at the moment!");
        List<EventResponseDto> allTrendingEventsView= new ArrayList<>();

        for(Event each: allTrendingEvents){
            allTrendingEventsView.add(changeToEventDto(each));
        }

        return allTrendingEventsView;
    }



    //service method for adding new event
    @Override
    public EventResponseDto addEvent(AddEventRequestDto addEventDto, MultipartFile coverImage) {

        boolean eventNameExists = eventRepository.existsByName(addEventDto.getEventName());

        if(eventNameExists) throw new ResourceAlreadyExistsException("Event name already exists");

        //saving the event location detail
        EventLocation eventLocation = eventLocationService.saveEventLocation(addEventDto.getLocationType(), addEventDto.getLocationName());

        //saving the event date details in the event date table
        EventDate eventDate = eventDateService.saveEventDate(addEventDto.getEventDateDetails());

        String coverImageUrl = cloudinaryUploadService.uploadImage(coverImage, "Event Cover Photo");

        EventTicket eventTicket= eventTicketService.saveEventTicket(addEventDto.getEventTicketDetails());

        EventVisibility eventVisibility = eventVisibilityService.saveEventVisibility(
                addEventDto.getEventVisibilityType(), addEventDto.getEventAccessPassword()
        );

        //getting the current user userName
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User organizer = userService.getUserByUsername(username);

        EventCategory eventType = eventTypeService.getEventTypeByTitle(addEventDto.ge)
        boolean hasStarring= addEventDto.isHasStarring();

        Event savedEvent = saveEvent(
                Event
                        .builder()
                        .eventLocation(eventLocation)
                        .eventDate(eventDate)
                        .eventCoverPage(coverImageUrl)
                        .hasStarring(hasStarring)
                        .aboutEvent(addEventDto.getAboutEvent())
                        .isPrivate(addEventDto.isPrivate())
                        .eventTicket(eventTicket)
                        .eventOrganizer(organizer)
                        .eventType(eve)
        )

        if(hasStarring){
            starringService.saveStarring(addEventDto.getStarrings(), savedEvent);
        }


        return null;
    }

    @Override
    public void addPromoCode(AddPromoCodeDto promoCodeDto){
        promoCodeService.addPromocode(promoCodeDto, getEventByName(promoCodeDto.getName()));
    }

}
