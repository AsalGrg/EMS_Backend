package com.backend.serviceImpls;

import com.backend.configs.JwtUtils;
import com.backend.dtos.*;
import com.backend.dtos.aboutEvent.EachStarring;
import com.backend.dtos.aboutEvent.EventDescriptionResponseDto;
import com.backend.dtos.aboutEvent.TicketDetail;
import com.backend.dtos.addEvent.*;
import com.backend.dtos.vendor.EventInternalDetailsDto;
import com.backend.dtos.vendor.PromoCodeDetailsDto;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.*;
import com.backend.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
public class EventServiceImplementation implements EventService {

    private final JwtUtils jwtUtils;
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
    private final VendorFollowerService vendorFollowerService;
    private final FavouriteEventService favouriteEventService;
    private final CloudinaryUploadService cloudinaryUploadService;


    @Autowired
    public EventServiceImplementation

            (EventRepository eventRepository,
             JwtUtils jwtUtils,
             UserService userService
            , PromoCodeService promoCodeService,VendorCredentialService vendorCredentialsService,
             EventLocationService eventLocationService, EventDateService eventDateService,
             EventTicketService eventTicketService, EventVisibilityService eventVisibilityService,
             EventCategoryService eventCategoryService,
             StarringService starringService,RoleService roleService, CloudinaryUploadService cloudinaryUploadServiceImpl,
             LocationTypeService locationTypeService,
             VendorFollowerService vendorFollowerService,
             EventPhysicalLocationDetailsService eventPhysicalLocationDetailsService,
             FavouriteEventService favouriteEventService){

        this.eventRepository= eventRepository;
        this.jwtUtils= jwtUtils;
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
        this.vendorFollowerService= vendorFollowerService;
        this.locationTypeService = locationTypeService;
        this.favouriteEventService= favouriteEventService;
    }

    public EventResponseDto changeToEventDto(Event event, EventPhysicalLocationDetails physicalLocationDetails){
//        return new EventResponseDto(event.getName(), event.getEventDate().;, event.getPublished_date(), event.getEntryFee(),event.getEventType().getTitle());
        EventFirstPageDetails eventFirstPageDetails = event.getEventFirstPageDetails();
        EventSecondPageDetails eventSecondPageDetails = event.getEventSecondPageDetails();
        EventThirdPageDetails eventThirdPageDetails = event.getEventThirdPageDetails();

        if (physicalLocationDetails == null) {
            return EventResponseDto.builder()
                    .eventId(event.getId())
                    .eventStatus(event.getEventStatus())
                    .pageStatus(event.getPageStatus())
                    .eventName(eventFirstPageDetails != null ? eventFirstPageDetails.getName() : null)
                    .eventCoverImgUrl(eventSecondPageDetails != null ? eventSecondPageDetails.getEventCoverPage() : null)
                    .startDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartDate() : null)
                    .endDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventEndDate() : null)
                    .startTime(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartTime() : null)
                    .ticketSalesEndDate(eventThirdPageDetails!=null && eventThirdPageDetails.getEventTicket()!=null? eventThirdPageDetails.getEventTicket().getTicketEndDate():null)
                    .category(eventFirstPageDetails != null && eventFirstPageDetails.getEventCategory() != null ? eventFirstPageDetails.getEventCategory().getTitle() : null)
                    .ticketType(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null && eventThirdPageDetails.getEventTicket().getTicketType() != null ? eventThirdPageDetails.getEventTicket().getTicketType().getTitle() : null)
                    .ticketPrice(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketPrice() : null)
                    .ticketsForSale(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketQuantity() : 0)
                    .ticketsSold(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketSold() : 0)
                    .organizerName(event.getEventOrganizer() != null ? event.getEventOrganizer().getUsername() : null)
                    .country("")
                    .location_display_name("")
                    .lat(0)
                    .lon(0)
                    .build();
        }
        return EventResponseDto.builder()
                .eventId(event.getId())
                .eventStatus(event.getEventStatus())
                .pageStatus(event.getPageStatus())
                .eventName(eventFirstPageDetails != null ? eventFirstPageDetails.getName() : null)
                .eventCoverImgUrl(eventSecondPageDetails != null ? eventSecondPageDetails.getEventCoverPage() : null)
                .startDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartDate() : null)
                .endDate(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventEndDate() : null)
                .startTime(eventFirstPageDetails != null && eventFirstPageDetails.getEventDate() != null ? eventFirstPageDetails.getEventDate().getEventStartTime() : null)
                .ticketSalesEndDate(eventThirdPageDetails!=null && eventThirdPageDetails.getEventTicket()!=null? eventThirdPageDetails.getEventTicket().getTicketEndDate():null)
                .category(eventFirstPageDetails != null && eventFirstPageDetails.getEventCategory() != null ? eventFirstPageDetails.getEventCategory().getTitle() : null)
                .ticketType(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null && eventThirdPageDetails.getEventTicket().getTicketType() != null ? eventThirdPageDetails.getEventTicket().getTicketType().getTitle() : null)
                .ticketPrice(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketPrice() : null)
                .ticketsForSale(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketQuantity() : 0)
                .ticketsSold(eventThirdPageDetails != null && eventThirdPageDetails.getEventTicket() != null ? eventThirdPageDetails.getEventTicket().getTicketSold() : 0)
                .organizerName(event.getEventOrganizer() != null ? event.getEventOrganizer().getUsername() : null)
                .country(physicalLocationDetails.getCountry())
                .location_display_name(physicalLocationDetails.getDisplayName())
                .lat(physicalLocationDetails.getLat())
                .lon(physicalLocationDetails.getLon())
                .build();
    }

    @Override
    public void likeEvent(int eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        favouriteEventService.addFavouriteEvent(getEventById(eventId),userService.getUserByUsername(username).getUserId() );
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
    public List<EventResponseDto> getAllVendorEventsSnippets() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        List<Event> events= eventRepository.getEventsByUser(user);
        List<EventResponseDto> eventResponseDtos= new ArrayList<>();

        for (Event event:
             events) {
            EventResponseDto eventResponseDto= changeToEventDto(event, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(event.getEventFirstPageDetails().getEventLocation()));
            eventResponseDtos.add(eventResponseDto);
        }
        return eventResponseDtos;
    }

    @Override
    public EventInternalDetailsDto getEventInternalDetails(int eventId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Event event= getEventById(eventId);
        User user = event.getEventOrganizer();

        if(!username.equals(user.getUsername())){
            throw new InternalServerError("Invalid request!");
        }

        EventResponseDto eventResponseDto= changeToEventDto(event, eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(event.getEventFirstPageDetails().getEventLocation()));

        List<PromoCode> eventPromoCodes = promoCodeService.getPromoCodesOfEvent(event);
        List<PromoCodeDetailsDto> promoCodeDetails = new ArrayList<>();

        for (PromoCode promoCode:
          eventPromoCodes   ) {

            String merit;

            if(promoCode.getPromoCodeType().getTitle().equals("Cash discount")){
                merit= "Rs "+promoCode.getDiscount()+" off";
            }else {
                merit= promoCode.getDiscount()+" % off";
            }

            promoCodeDetails.add(
                    PromoCodeDetailsDto
                            .builder()
                            .promCodeName(promoCode.getTitle())
                            .expiryDate(promoCode.getExpiryDate())
                            .merit(merit)
                            .applicableFrom(promoCode.getApplicableOn())
                            .limit(promoCode.getLimit())
                            .available(promoCode.getAvailableQuantity())
                            .isActive(promoCode.isActive())
                            .build()
            );
        }
        return EventInternalDetailsDto
                .builder()
                .eventBasicDetails(eventResponseDto)
                .promoCodeDetailsDtos(promoCodeDetails)
                .eventOrders(new ArrayList<>())//for testing only
                .build();
    }

    public EventDescriptionResponseDto getAboutEventByEventId(int eventId, HttpServletRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return covertToEventDescriptionDto(getEventById(eventId), request);
    }

    @Override
    public List<EventResponseDto> getAllUserLikedEvents() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<FavouriteEvents> userFavouriteEvents= favouriteEventService.getFavouriteEventsOfUser(userService.getUserByUsername(username).getUserId());
        List<EventResponseDto> eventResponseDtos = new ArrayList<>();

        for (FavouriteEvents favouriteEvent:
             userFavouriteEvents) {
            Event event= favouriteEvent.getEvent();
            EventPhysicalLocationDetails eventPhysicalLocationDetails= eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(event.getEventFirstPageDetails().getEventLocation());
            eventResponseDtos.add(changeToEventDto(event, eventPhysicalLocationDetails));
        }
        return eventResponseDtos;
    }

    @Override
    public ApplyPromoCodeResponseDto isPromoCodeValid(String promoCode , int eventId, double totalAmount){

        Event eventDetails = getEventById(eventId);
        return promoCodeService.isPromoCodeValid(promoCode, eventDetails, totalAmount);
    }
    private EventDescriptionResponseDto covertToEventDescriptionDto(Event event, HttpServletRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        EventTicket eventTicket= event.getEventThirdPageDetails().getEventTicket();
        TicketDetail ticketDetail = TicketDetail
                .builder()
                .ticketBookedQuantity(0)
                .ticketName(eventTicket.getTicketName())
                .ticketPrice(eventTicket.getTicketPrice())
                .ticketType(eventTicket.getTicketType().getTitle())
                .ticketAvailableQuantity(eventTicket.getTicketQuantity())//to be ticket booked quantity - ticket initial quantity
//                .hasPromoCode(promoCodeService.checkPromoCodeExistsInEvent(event))
                .hasPromoCode(true)
                .build();

        List<EachStarring> starrings = new ArrayList<>();

        EventStarring eventStarring = starringService.getEventStarringByEventId(event.getId());

        if(eventStarring!=null){

            for(int i =1; i<=5; i++){

                Method methodStarringName = eventStarring.getClass().getMethod("getStarring"+i+"Name");
                Method methodStarringImage = eventStarring.getClass().getMethod("getStarring" + i + "Photo");

                String starringName= (String) methodStarringName.invoke(eventStarring);
                String starringPhoto= (String) methodStarringImage.invoke(eventStarring);

                if(starringPhoto==null){
                    continue;
                }
                starrings.add(
                        EachStarring
                                .builder()
                                .starringName(starringName)
                                .starringPhoto(starringPhoto)
                                .build()
                );
            }
        }

        EventFirstPageDetails eventFirstPageDetails= event.getEventFirstPageDetails();
        EventSecondPageDetails eventSecondPageDetails= event.getEventSecondPageDetails();

        String usernameFromToken = jwtUtils.customFilterCheck(request);
        User vendor = event.getEventOrganizer();

        VendorResponseDto vendorResponseDto;

        if(usernameFromToken==null){
            vendorResponseDto= VendorResponseDto
                    .builder()
                    .vendorProfile(vendor.getUserDp())
                    .vendorId(vendor.getUserId())
                    .vendorName(vendor.getUsername())
                    .hasFollowed(false)
                    .vendorFollowers(vendorFollowerService.getNoOfFollowers(vendor.getUserId()))
                    .build();
            return EventDescriptionResponseDto
                    .builder()
                    .eventId(event.getId())
                    .eventEndTime(eventFirstPageDetails.getEventDate().getEventEndTime())
                    .eventStartTime(eventFirstPageDetails.getEventDate().getEventEndTime())
                    .eventEndDate(eventFirstPageDetails.getEventDate().getEventEndDate())
                    .eventStartDate(eventFirstPageDetails.getEventDate().getEventStartDate())
                    .starrings(starrings)
                    .aboutEvent(eventSecondPageDetails.getAboutEvent())
                    .coverImage(eventSecondPageDetails.getEventCoverPage())
                    .eventTitle(eventFirstPageDetails.getName())
                    .hasStarring(eventSecondPageDetails.isHasStarring())
                    .physicalLocationDetails(eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(event.getEventFirstPageDetails().getEventLocation()))
                    .locationType(eventFirstPageDetails.getEventLocation().getLocationType().getLocationTypeTitle())
                    .vendorDetails(vendorResponseDto)
                    .ticketDetails(ticketDetail)
                    .hasLiked(false)
                    .build();

        }else{
            vendorResponseDto=VendorResponseDto
                    .builder()
                    .vendorProfile(vendor.getUserDp())
                    .vendorId(vendor.getUserId())
                    .vendorName(vendor.getUsername())
                    .hasFollowed(vendorFollowerService.checkIfHasFollowedVendor(vendor.getUserId(), userService.getUserByUsername(usernameFromToken).getUserId()))
                    .vendorFollowers(vendorFollowerService.getNoOfFollowers(vendor.getUserId()))
                    .build();

            return EventDescriptionResponseDto
                    .builder()
                    .eventId(event.getId())
                    .eventEndTime(eventFirstPageDetails.getEventDate().getEventEndTime())
                    .eventStartTime(eventFirstPageDetails.getEventDate().getEventEndTime())
                    .eventEndDate(eventFirstPageDetails.getEventDate().getEventEndDate())
                    .eventStartDate(eventFirstPageDetails.getEventDate().getEventStartDate())
                    .starrings(starrings)
                    .aboutEvent(eventSecondPageDetails.getAboutEvent())
                    .coverImage(eventSecondPageDetails.getEventCoverPage())
                    .eventTitle(eventFirstPageDetails.getName())
                    .hasStarring(eventSecondPageDetails.isHasStarring())
                    .physicalLocationDetails(eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(event.getEventFirstPageDetails().getEventLocation()))
                    .locationType(eventFirstPageDetails.getEventLocation().getLocationType().getLocationTypeTitle())
                    .vendorDetails(vendorResponseDto)
                    .ticketDetails(ticketDetail)
                    .hasLiked(favouriteEventService.checkIfHasLikedEvent(userService.getUserByUsername(usernameFromToken).getUserId(), event.getId()))
                    .build();
        }
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
    public List<EventResponseDto> getEventByPlace(String place) {

        List<Event> events;
        List<EventResponseDto> eventResponseDtos= new ArrayList<>();

        if(place.equals("online")){
            events= eventRepository.getAllOnlineEvents();
        }else{
            events= eventRepository.getEventByLocation(place);
        }

        for(Event event: events){
            if(event.getEventFirstPageDetails().getEventLocation().isPhysical()){
                eventResponseDtos.add(changeToEventDto(event, getEventPhysicalLocationDetails(event.getEventFirstPageDetails().getEventLocation())));
            }
            else {
                eventResponseDtos.add(changeToEventDto(event,null));
            }
        }
        return eventResponseDtos;
    }

    @Override
    public CategoryDetailsDto getEventByTypeAndLocation(String type, String location, HttpServletRequest request) {

        String username = jwtUtils.customFilterCheck(request);

        List<EventResponseDto> eventResponseDtos= new ArrayList<>();
        List<Event> events;

        if(location.equals("online")){
            events= eventRepository.getOnlineEventsByType(type);
        }else {
            events= eventRepository.getEventByTypeAndLocation(type, location);
        }
        List<VendorResponseDto> vendorResponseDtos= new ArrayList<>();
        Set<Integer> vendorIds= new HashSet<>();


        for(Event event: events){
            vendorIds.add(event.getEventOrganizer().getUserId());
            if(event.getEventFirstPageDetails().getEventLocation().isPhysical()){
                eventResponseDtos.add(changeToEventDto(event, getEventPhysicalLocationDetails(event.getEventFirstPageDetails().getEventLocation())));
            }
            else {
                eventResponseDtos.add(changeToEventDto(event,null));
            }
        }

        for(Integer vendorId: vendorIds){
            User user = userService.getUserByUserId(vendorId);

            if(username!=null){
                vendorResponseDtos.add(
                        VendorResponseDto.builder()
                                .vendorId(vendorId)
                                .vendorName(user.getUsername())
                                .vendorFollowers(vendorFollowerService.getNoOfFollowers(vendorId))
                                .vendorProfile(user.getUserDp())
                                .hasFollowed(vendorFollowerService.checkIfHasFollowedVendor(vendorId, userService.getUserByUsername(username).getUserId()))
                                .build()
                );
            }else {
                vendorResponseDtos.add(
                        VendorResponseDto.builder()
                                .vendorId(vendorId)
                                .vendorName(user.getUsername())
                                .vendorFollowers(vendorFollowerService.getNoOfFollowers(vendorId))
                                .vendorProfile(user.getUserDp())
                                .hasFollowed(false)
                                .build()
                );
            }
        }


        return CategoryDetailsDto.builder()
                .categoryEvents(eventResponseDtos)
                .categoryVendors(vendorResponseDtos)
                .build();
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

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Event savedEvent = saveEvent(
                    //TODO: add organizer details
                    Event
                            .builder()
                            .eventStatus("draft")
                            .eventOrganizer(userService.getUserByUsername(username))
                            .pageStatus(0)
                            .eventFirstPageDetails(savedFirstPageDetails)
                            .build()
            );

            log.info("Event location typeee: "+ eventLocation.getLocationType().getLocationTypeTitle());
            if(eventLocation.getLocationType().getLocationTypeTitle().equals("Venue")){
                log.info("Event location typeee: "+ eventLocation.getLocationType().getLocationTypeTitle());
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

        log.info("Update Event location typeee: "+ eventLocation.getLocationType().getLocationTypeTitle());
        if(eventLocation.getLocationType().getLocationTypeTitle().equals("Venue")){
            EventPhysicalLocationDetails eventPhysicalLocationDetailsFromDB = eventPhysicalLocationDetailsService.getEventPhysicalLocationDetailsByEventLocation(eventLocation);

            if(eventPhysicalLocationDetailsFromDB==null){
                eventPhysicalLocationDetailsService.savePhysicalLocationDetails(eventPhysicalLocationDetails, eventLocation);
            }else {
                eventPhysicalLocationDetailsService.updatePhysicalLocationDetails(
                        EventPhysicalLocationDetails
                                .builder()
                                .id(eventPhysicalLocationDetailsFromDB.getId())
                                .eventLocation(eventLocation)
                                .country(eventPhysicalLocationDetails.getCountry())
                                .lon(eventPhysicalLocationDetails.getLon())
                                .lat(eventPhysicalLocationDetails.getLat())
                                .displayName(eventPhysicalLocationDetails.getDisplayName())
                                .build()
                );
            }
        }


        return event.getId();
    }


    @Override
    public void addEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails) {
        if (addEventSecondPageDto.getEventId()==null){
            throw  new InternalServerError("Invalid event creation attempt");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Event event = getEventById(addEventSecondPageDto.getEventId());


        if(!Objects.equals(username, event.getEventOrganizer().getUsername())){
            throw new NotAuthorizedException("Not authorized for the action");
        }
        EventSecondPageDetails eventSecondPageDetails = event.getEventSecondPageDetails();

        if(eventSecondPageDetails==null){
            String coverImageUrl= null;
            String coverImageName= null;
            if(addEventSecondPageDto.getEventCoverImage()!=null) {
                coverImageUrl = cloudinaryUploadService.uploadImage(addEventSecondPageDto.getEventCoverImage(), "Event Cover Photo");
                coverImageName= addEventSecondPageDto.getEventCoverImage().getOriginalFilename();
            }
            EventSecondPageDetails savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                    EventSecondPageDetails
                            .builder()
                            .eventCoverPage(coverImageUrl)
                            .coverImgName(coverImageName)
                            .aboutEvent(addEventSecondPageDto.getAboutEvent())
                            .hasStarring(addEventSecondPageDto.isHasStarring())
                            .build()
            );
            event.setEventSecondPageDetails(savedEventSecondPageDetails);
            event.setPageStatus(1);
            saveEvent(event);
        }else{
            updateEventSecondPageDetails(addEventSecondPageDto, eventStarringDetails, event);
        }

        if(addEventSecondPageDto.isHasStarring()){
            starringService.saveStarring(eventStarringDetails, event);
        }
    }

    public void updateEventSecondPageDetails(AddEventSecondPageDto addEventSecondPageDto, EventStarringDetails eventStarringDetails, Event event) {

        EventSecondPageDetails savedEventSecondPageDetails;

        if(addEventSecondPageDto.getEventCoverImage()==null){
            savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                    EventSecondPageDetails
                            .builder()
                            .id(event.getEventSecondPageDetails().getId())
                            .eventCoverPage(null)
                            .coverImgName(null)
                            .aboutEvent(addEventSecondPageDto.getAboutEvent())
                            .hasStarring(addEventSecondPageDto.isHasStarring())
                            .build()
            );
        }else {
            if (event.getEventSecondPageDetails().getCoverImgName()==null || !event.getEventSecondPageDetails().getCoverImgName().equals(addEventSecondPageDto.getEventCoverImage().getOriginalFilename())) {
                String coverImageUrl = cloudinaryUploadService.uploadImage(addEventSecondPageDto.getEventCoverImage(), "Event Cover Photo");
                savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                        EventSecondPageDetails
                                .builder()
                                .id(event.getEventSecondPageDetails().getId())
                                .eventCoverPage(coverImageUrl)
                                .coverImgName(addEventSecondPageDto.getEventCoverImage().getOriginalFilename())
                                .aboutEvent(addEventSecondPageDto.getAboutEvent())
                                .hasStarring(addEventSecondPageDto.isHasStarring())
                                .build()
                );
            }else{
                savedEventSecondPageDetails = eventRepository.saveSecondPageDetails(
                        EventSecondPageDetails
                                .builder()
                                .id(event.getEventSecondPageDetails().getId())
                                .eventCoverPage(event.getEventSecondPageDetails().getEventCoverPage())
                                .coverImgName(event.getEventSecondPageDetails().getCoverImgName())
                                .aboutEvent(addEventSecondPageDto.getAboutEvent())
                                .hasStarring(addEventSecondPageDto.isHasStarring())
                                .build()
                );
            }
        }

        event.setEventSecondPageDetails(savedEventSecondPageDetails);
        saveEvent(event);
    }


    @Override
    public void addEventThirdPageDetails(EventTicketDetailsDto eventTicketDetailsDto) {

        if (eventTicketDetailsDto.getEventId() == null) {
            throw new InternalServerError("Invalid event creation attempt");
        }
        Event event = eventRepository.getEventById(eventTicketDetailsDto.getEventId());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!Objects.equals(username, event.getEventOrganizer().getUsername())) {
            throw new NotAuthorizedException("Not authorized for the action");

        }
            EventThirdPageDetails eventThirdPageDetails = event.getEventThirdPageDetails();

            if (eventThirdPageDetails == null) {
                EventTicket eventTicket = eventTicketService.saveEventTicket(eventTicketDetailsDto);
                eventThirdPageDetails = EventThirdPageDetails.builder()
                        .eventTicket(eventTicket)
                        .build();
                EventThirdPageDetails savedEventThirdPageDetails = eventRepository.saveThirdPageDetails(eventThirdPageDetails);
                //updating the event
                event.setEventThirdPageDetails(savedEventThirdPageDetails);
                event.setPageStatus(2);
                eventRepository.saveEvent(event);


            } else {
                //update
                eventTicketService.updateEventTicket(eventTicketDetailsDto, eventThirdPageDetails.getEventTicket().getId());
            }

        }

    @Override
    public void addEventFourthPageDetails(AddEventFourthPageDto addEventFourthPageDto) {

        if (addEventFourthPageDto.getEventId()==null){
            throw  new InternalServerError("Invalid event creation attempt");
        }
        Event event = eventRepository.getEventById(addEventFourthPageDto.getEventId());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!Objects.equals(username, event.getEventOrganizer().getUsername())) {
            throw new NotAuthorizedException("Not authorized for the action");
        }

        EventVisibility eventVisibilityFromDb = event.getEventVisibility();

        if(eventVisibilityFromDb==null){
            eventVisibilityFromDb= eventVisibilityService.saveEventVisibility(addEventFourthPageDto.getVisibilityOption(), addEventFourthPageDto.getAccessPassword());
            event.setEventVisibility(eventVisibilityFromDb);
            event.setEventStatus("completed");
            event.setPageStatus(3);
            eventRepository.saveEvent(event);
        }else {
            log.info("Event Access Password: "+ addEventFourthPageDto.getAccessPassword());
            eventVisibilityService.updateEventVisibility(addEventFourthPageDto.getVisibilityOption(), addEventFourthPageDto.getAccessPassword(), eventVisibilityFromDb.getId());
        }
    }

    @Override
    public void addPromoCode(AddPromoCodeDto promoCodeDto){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        Event event= getEventById(promoCodeDto.getEventId());

        if(user.getUserId()!= event.getEventOrganizer().getUserId()){
            throw new NotAuthorizedException("Unauthorized add request");
        }

        promoCodeService.addPromocode(promoCodeDto, getEventById(promoCodeDto.getEventId()));
    }

    @Override
    public void activePromoCode(int promoCodeId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PromoCode promoCode= promoCodeService.getPromoCodeByid(promoCodeId);
        if(!promoCode.getEvent().getEventOrganizer().getUsername().equals(username))throw new NotAuthorizedException("Not authorized for the given action");

        promoCodeService.activatePromoCode(promoCodeId);
    }

    @Override
    public void deactivePromoCode(int promoCodeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PromoCode promoCode= promoCodeService.getPromoCodeByid(promoCodeId);
        if(!promoCode.getEvent().getEventOrganizer().getUsername().equals(username))throw new NotAuthorizedException("Not authorized for the given action");

        promoCodeService.deactivatePromoCode(promoCodeId);
    }

}
