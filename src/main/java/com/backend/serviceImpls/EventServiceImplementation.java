package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.*;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.*;
import com.backend.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;

    private LocationTypeService locationTypeService;
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
             LocationTypeService locationTypeService,
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
        this.locationTypeService = locationTypeService;
    }

    public EventResponseDto changeToEventDto(Event event, EventPhysicalLocationDetails physicalLocationDetails){
//        return new EventResponseDto(event.getName(), event.getEventDate().;, event.getPublished_date(), event.getEntryFee(),event.getEventType().getTitle());
/*        if(physicalLocationDetails==null) {
            return EventResponseDto.builder().
                    eventName(event.getName())
                    .eventCoverImgUrl(event.getEventCoverPage())
                    .startDate(event.getEventDate().getEventStartDate())
                    .endDate(event.getEventDate().getEventEndDate())
                    .category(event.getEventCategory().getTitle())
                    .ticketType(event.getEventTicket().getTicketType().getTitle())
                    .ticketPrice(event.getEventTicket().getTicketPrice())
                    .build();
        }
        return EventResponseDto.builder().
                eventName(event.getName())
                .eventCoverImgUrl(event.getEventCoverPage())
                .startDate(event.getEventDate().getEventStartDate())
                .endDate(event.getEventDate().getEventEndDate())
                .category(event.getEventCategory().getTitle())
                .ticketType(event.getEventTicket().getTicketType().getTitle())
                .ticketPrice(event.getEventTicket().getTicketPrice())
                .country(physicalLocationDetails.getCountry())
                .location_display_name(physicalLocationDetails.getDisplayName())
                .lat(physicalLocationDetails.getLat())
                .lon(physicalLocationDetails.getLon())
                .build();*/
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
    public List<EventResponseDto> getQuickSearchResult(String keyword) {
        List<Event> quickSearchEvents = eventRepository.getQuickSearchResult(keyword);
        if(quickSearchEvents.isEmpty()) throw new ResourceNotFoundException("Searched event not found at the moment, you may like other events");

        List<EventResponseDto> filteredEventsView = new ArrayList<>();

        for(Event event: quickSearchEvents){
            if(event.getEventFirstPageDetails().getEventLocation().isPhysical()){
                filteredEventsView.add(changeToEventDto(event, getEventPhysicalLocationDetails(event.getEventFirstPageDetails().getEventLocation())));
            }
            else {
                filteredEventsView.add(changeToEventDto(event,null));
            }
        }

        return filteredEventsView;
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
                physicalEventsResponse.add(changeToEventDto(each,getEventPhysicalLocationDetails(each.getEventFirstPageDetails().getEventLocation())));
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

/*
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


        return null;*/
        return null;
    }

    @Override
    public Integer addFirstPageDetails(AddEventFirstPageDto addEventFirstPageDto, EventDateDetailsDto eventDateDetailsDto, EventPhysicalLocationDetailsDto eventPhysicalLocationDetails) {

        if(addEventFirstPageDto.getEventId()==null){
            boolean eventNameExists = eventRepository.existsByName(addEventFirstPageDto.getEventName());
            log.info("Event name exists: "+ eventNameExists);
            if(eventNameExists) throw new ResourceAlreadyExistsException("Event name already exists");

            //saving the event location detail
            EventLocation eventLocation = eventLocationService.saveEventLocation(addEventFirstPageDto.getLocationType(), addEventFirstPageDto.getLocationName());

            //saving the event date details in the event date table
            EventDate eventDate = eventDateService.saveEventDate(
                    EventDate
                            .builder()
                            .eventStartDate(eventDateDetailsDto.getEventStartDate())
                            .eventStartTime(eventDateDetailsDto.getEventStartTime())
                            .eventEndDate(eventDateDetailsDto.getEventEndDate())
                            .eventEndTime(eventDateDetailsDto.getEventEndTime())
                            .eventPublishDate(LocalDateTime.now())
                            .displayStartTime(eventDateDetailsDto.isDisplayStartTime())
                            .displayEndTime(eventDateDetailsDto.isDisplayEndTime())
                            .build()
            );

            EventFirstPageDetails eventFirstPageDetails= EventFirstPageDetails
                    .builder()
                    .eventLocation(eventLocation)
                    .eventCategory(eventCategoryService.getEventCategoryByName(addEventFirstPageDto.getEventCategory()))
                    .eventDate(eventDate)
                    .name(addEventFirstPageDto.getEventName())
                    .build();

            EventFirstPageDetails savedFirstPageDetails = eventRepository.saveFirstPageDetails(eventFirstPageDetails);

            Event savedEvent = saveEvent(
                    //TODO: add organizer details
                    Event
                            .builder()
                            .eventFirstPageDetails(savedFirstPageDetails)
                            .build()
            );

            if(eventLocation.getLocationType().getLocationTypeTitle().equals("venue")){
                eventPhysicalLocationDetailsService.savePhysicalLocationDetails(eventPhysicalLocationDetails, eventLocation);
            }
            return savedEvent.getId();

        }else {
            return updateFirstPageDetails(addEventFirstPageDto, eventDateDetailsDto, eventPhysicalLocationDetails, addEventFirstPageDto.getEventId());
        }
    }

    public Integer updateFirstPageDetails(AddEventFirstPageDto addEventFirstPageDto, EventDateDetailsDto eventDateDetailsDto, EventPhysicalLocationDetailsDto eventPhysicalLocationDetails, Integer eventId){

        boolean eventNameExists = eventRepository.existsByNameButNotForDraft(addEventFirstPageDto.getEventName(), eventId);

        if(eventNameExists) throw  new ResourceAlreadyExistsException("Event name already exists exception");
        Event event= eventRepository.getEventById(eventId);

        EventFirstPageDetails eventFirstPageDetails= event.getEventFirstPageDetails();

        EventDate eventDate= eventFirstPageDetails.getEventDate();

        EventDate updatedEventDate = EventDate
                .builder()
                .id(eventDate.getId())
                .eventStartDate(eventDateDetailsDto.getEventStartDate())
                .eventStartTime(eventDateDetailsDto.getEventStartTime())
                .eventEndDate(eventDateDetailsDto.getEventEndDate())
                .eventEndTime(eventDateDetailsDto.getEventEndTime())
                .eventPublishDate(LocalDateTime.now())
                .displayStartTime(eventDateDetailsDto.isDisplayStartTime())
                .displayEndTime(eventDateDetailsDto.isDisplayEndTime())
                .build();

        eventDateService.saveEventDate(updatedEventDate);

        EventLocation eventLocation = eventFirstPageDetails.getEventLocation();
        EventLocation updatedEventLocation = EventLocation
                .builder()
                .id(eventLocation.getId())
                .isPhysical(addEventFirstPageDto.isPhysical())
                .locationName(addEventFirstPageDto.getLocationName())
                .locationType(locationTypeService.getLocationTypeByName(addEventFirstPageDto.getLocationType()))
                .build();
        eventLocationService.updateEventLocation(updatedEventLocation);


        EventCategory eventCategory = eventCategoryService.getEventCategoryByName(addEventFirstPageDto.getEventCategory());

        EventFirstPageDetails updatedEventFirstPageDetails= EventFirstPageDetails
                    .builder()
                    .id(eventFirstPageDetails.getId())
                    .eventLocation(eventLocation)
                    .eventCategory(eventCategory)
                    .eventDate(eventDate)
                    .name(addEventFirstPageDto.getEventName())
                .build();

        event.setEventFirstPageDetails(eventRepository.saveFirstPageDetails(updatedEventFirstPageDetails));
        eventRepository.saveEvent(event);

        return event.getId();
    }


    @Override
    public void addEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails) {
        if (addEventSecondPageDto.getEventId()==null){
            throw  new InternalServerError("Invalid event creation attempt");
        }

        Event event = getEventById(addEventSecondPageDto.getEventId());

        EventSecondPageDetails eventSecondPageDetails = event.getEventSecondPageDetails();

        if(eventSecondPageDetails==null){
            String coverImageUrl = cloudinaryUploadService.uploadImage(addEventSecondPageDto.getEventCoverImage(), "Event Cover Photo");

            EventSecondPageDetails savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                    EventSecondPageDetails
                            .builder()
                            .eventCoverPage(coverImageUrl)
                            .aboutEvent(addEventSecondPageDto.getAboutEvent())
                            .hasStarring(addEventSecondPageDto.isHasStarring())
                            .build()
            );

            event.setEventSecondPageDetails(savedEventSecondPageDetails);
            saveEvent(event);
        }else{

        }

        if(addEventSecondPageDto.isHasStarring()){
            starringService.saveStarring(eventStarringDetails, event);
        }
    }


    public void updateEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails){
        String coverImageUrl = cloudinaryUploadService.uploadImage(addEventSecondPageDto.getEventCoverImage(), "Event Cover Photo");

        EventSecondPageDetails savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                EventSecondPageDetails
                        .builder()
                        .eventCoverPage(coverImageUrl)
                        .aboutEvent(addEventSecondPageDto.getAboutEvent())
                        .hasStarring(addEventSecondPageDto.isHasStarring())
                        .build()
        );

        event.setEventSecondPageDetails(savedEventSecondPageDetails);
        saveEvent(event);
    }

    @Override
    public void addPromoCode(AddPromoCodeDto promoCodeDto){
        promoCodeService.addPromocode(promoCodeDto, getEventByName(promoCodeDto.getName()));
    }

}
