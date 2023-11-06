package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.*;
import com.backend.repositories.*;
import com.backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;

    private final EventTypeServiceImplementation eventTypeService;

    private final UserServiceImplementation userService;

    private final PromoCodeServiceImplementation promoCodeService;

    private final EventAccessRequestServiceImplementation eventAccessRequestService;

    private final VendorCredentialServiceImplementation vendorCredentialsService;

    private final RoleServiceImplementation roleService;

    private final CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl;


    @Autowired
    public EventServiceImplementation

            (EventRepository eventRepository, EventTypeServiceImplementation eventTypeService, UserServiceImplementation userService
            , PromoCodeServiceImplementation promoCodeService, EventAccessRequestServiceImplementation eventAccessRequestService,
             VendorCredentialServiceImplementation vendorCredentialsService,
             RoleServiceImplementation roleService, CloudinaryUploadServiceImplementation cloudinaryUploadServiceImpl){

        this.eventRepository= eventRepository;
        this.eventTypeService= eventTypeService;
        this.userService= userService;
        this.promoCodeService= promoCodeService;
        this.eventAccessRequestService= eventAccessRequestService;
        this.vendorCredentialsService= vendorCredentialsService;
        this.roleService= roleService;
        this.cloudinaryUploadServiceImpl= cloudinaryUploadServiceImpl;
    }

    public EventResponseDto changeToEventDto(Event event){
        return new EventResponseDto(event.getAccessToken(), event.getName(), event.getLocation(), event.getPublished_date(), event.getEventDate(), event.getEntryFee(),event.getEventType().getTitle());
    }

    @Override
    public Event getEventById(int id) {
        Event event=  eventRepository.findEventById(id);
        if(event==null){
            throw new ResourceNotFoundException("Event with the given id does not exist");
        }

        return event;
    }

    public Event getEventByName(String name){
        return eventRepository.findEventByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid event name '"+name+"'"));
    }

    public void saveEvent(Event event){
        eventRepository.save(event);
    }

    @Override
    public List<Event> getEventByPlace(String place) {
        List<Event> events= eventRepository.findEventByLocationAndIsAcceptedAndIsDeclined(place, true, false);

        if (events.isEmpty()){
            throw new ResourceNotFoundException("Events for the given place are currently not available");
        }

        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        return eventRepository.findEventByType(type);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findByIsAcceptedAndIsDeclined(true, false);

        if(events.isEmpty()){
            throw new ResourceNotFoundException("Currently no available events");
        }

        return events;
    }

    //service method to get all the events from the filter such as time, date and place and venue
    public List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto){
        List<Event> filterEventsList = eventRepository.findByLocationAndEventTimeAndEventDateAndEventType_TitleAndIsAccepted(
                searchEventByFilterDto.getLocation(),
                searchEventByFilterDto.getEvent_time(),
                searchEventByFilterDto.getEvent_date(),
                searchEventByFilterDto.getEvent_category(),
                true
        );

        if(filterEventsList.isEmpty()) throw new ResourceNotFoundException("Searched event not found at the moment, you may like other events");

        List<EventResponseDto> filteredEventsView = new ArrayList<>();

        for(Event event: filterEventsList){
            filteredEventsView.add(changeToEventDto(event));
        }

        return filteredEventsView;
    }

    //service handler method to get the trending events
    public List<EventResponseDto> getTrendingEvents(){
        List<Event> allTrendingEvents= eventRepository.findAllByIsAcceptedAndEventDateAfterOrderByTicketSoldDesc(true, LocalDate.now());

        if(allTrendingEvents.isEmpty()) throw new ResourceNotFoundException("No events at the moment!");
        List<EventResponseDto> allTrendingEventsView= new ArrayList<>();

        for(Event each: allTrendingEvents){
            allTrendingEventsView.add(changeToEventDto(each));
        }

        return allTrendingEventsView;
    }



    //service method for adding new event
    @Override
    public EventResponseDto addEvent(AddEventRequestDto addEventDto) {

        //checking if the event name already exists or not
        boolean eventNameExists= eventRepository.existsByName(addEventDto.getName());

        //if the event name matches
        if(eventNameExists){
            throw new ResourceAlreadyExistsException("Event name already exists");
        }

        VendorCredential vendorCredential= vendorCredentialsService.findVendorCredentialByVendorName(userService.getUserByUsername(addEventDto.getEvent_vendor()));

        if(vendorCredential.isDeclined() && !vendorCredential.isVerified() && vendorCredential.isTerminated()){
            throw new ResourceNotFoundException("Invalid vendor name");
        }


        if(addEventDto.getPromoCodes()!=null){

            for(PromoCode promoCode: addEventDto.getPromoCodes()){
                if(promoCodeService.checkPromoCodeExistsByTitle(promoCode.getName())){
                    throw new ResourceAlreadyExistsException(promoCode.getName()+" already exits!");
                }
            }
        }

        //setting the event details from the dto to the event object
        Event event= new Event();
        event.setName(addEventDto.getName());
        event.setLocation(addEventDto.getLocation());
        event.setPublished_date(addEventDto.getPublished_date());
        event.setEventDate(addEventDto.getEvent_date());
        event.setDescription(addEventDto.getDescription());
        event.setPrivate(addEventDto.isPrivate());
        event.setEntryFee(addEventDto.getEntryFee());
        event.setSeats(addEventDto.getSeats());
        event.setEventOrganizer(userService.getUserByUsername(addEventDto.getEvent_organizer()));

        //setting the event isAccepted and isDeclined to false at first, which needs to be either true false after the event vendor response.
        event.setAccepted(false);
        event.setDeclined(false);

        //checking if the event is private
        if(addEventDto.isPrivate()){

            //generating random access token for the event
            String accessToken = UUID.randomUUID().toString();
            event.setAccessToken(accessToken);


            //checking if the user group is empty or not
            if(addEventDto.getEvent_group()!=null){

                List<String> user_groups= addEventDto.getEvent_group();

                //event organizer and event_organizer is automatically added
                user_groups.add(addEventDto.getEvent_organizer());
                user_groups.add(addEventDto.getEvent_vendor());

                System.out.println(user_groups);

                Set<User> usersInvited= new HashSet<>();

                for(String usernameOrEmail: user_groups){

                    //checking each username provided in the user group set and if exists in the database
                    User user= userService.getUserByUsernameOrEmail(usernameOrEmail);

                    //adding the user details in the list
                    usersInvited.add(user);
                }
                //adding the list of the user group in the event object
                event.setEvent_group(usersInvited);
            }
        }

        if(addEventDto.getEventCoverPhoto()!=null){
            String imageUrl = cloudinaryUploadServiceImpl.uploadImage(addEventDto.getEventCoverPhoto(), "Event Photos");
            event.setEventCoverImage(imageUrl);
        }


        //other entities such as event group are to be set
        event.setEventType(eventTypeService.getEventTypeByTitle(addEventDto.getEventType()));

        //adding the event in the database
        eventRepository.save(event);

        if(addEventDto.getPromoCodes()!=null) {

            for(PromoCode promoCode: addEventDto.getPromoCodes()) {
                PromoCode savePromoCode = new PromoCode();
                savePromoCode.setName(promoCode.getName());
                savePromoCode.setDiscount_amount(promoCode.getDiscount_amount());
                savePromoCode.setEvent(event);

                promoCodeService.savePromoCode(savePromoCode);
            }

        }

        return changeToEventDto(event);
    }

    //method for vendor to accept the addEvent requests from the client i.e. event hoster

    @Override
    public void addEventVendorRequestAction(String username, String action, String eventName){
        User user= userService.getUserByUsername(username);

        Event eventDetails= eventRepository.findEventByName(eventName).orElseThrow(()->
                new ResourceNotFoundException("Invalid Event name"));

        if(!user.getUserRoles().contains(roleService.findRoleByTitle("VENDOR"))
                &&
                !user.getUsername().equals(eventDetails.getEventVendor().getUsername())){

            throw new NotAuthorizedException("Access Denied: You do not have the necessary permissions to perform this action.");
        }

        switch (action){

            case "accept" -> {
                eventDetails.setAccepted(true);
                eventRepository.save(eventDetails);
            }

            case "reject" ->{
                eventDetails.setDeclined(true);
                eventRepository.save(eventDetails);
            }
        }


    }

    @Override
    public EventResponseDto getEventByAccessToken(String accessToken, String username) {
        if(username==null){
            throw new NotAuthorizedException();
        }

        Event event= eventRepository.findByAccessToken(accessToken)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid Access Token"));

        Set<User> usersInvited= event.getEvent_group();

        for(User user: usersInvited){
            if (user.getUsername().equals(username)){
                return changeToEventDto(event);
            }
        }

        throw new NotAuthorizedException("You are not allowed to access the event");
    }



    //service method to make event access request

    @Override
    public void makeEventAccessRequest(String username, String accessToken){

        EventAccessRequest existingRequest = eventAccessRequestService.findRequestByUserAndEvent(
                userService.getUserByUsername(username),
                eventRepository.findByAccessToken(accessToken).get()
        );

        if(existingRequest!=null){
            throw new ResourceAlreadyExistsException("Request has already been collected");
        }

        existingRequest= new EventAccessRequest();
        existingRequest.setUser(userService.getUserByUsername(username));
        existingRequest.setEvent(eventRepository.findByAccessToken(accessToken).get());

        eventAccessRequestService.saveEventAccessRequest(existingRequest);

    }

    //service method handler for getting the event access requests
    @Override
    public List<EventAccessRequestsView> getEventAccessRequests(String username) {

       List<EventAccessRequest> allEventAccessRequests=  eventAccessRequestService.getEventsAccessRequestByEventOrg(username);

       List<EventAccessRequestsView> eventAccessRequestsViews= new ArrayList<>();
       for(EventAccessRequest eventAccessRequest: allEventAccessRequests){

           EventAccessRequestsView eventAccessRequestsView= new EventAccessRequestsView();
           eventAccessRequestsView.setUsername(eventAccessRequest.getUser().getUsername());
           eventAccessRequestsView.setEmail(eventAccessRequest.getUser().getEmail());
           eventAccessRequestsView.setEvent_name(eventAccessRequest.getEvent().getName());

           eventAccessRequestsViews.add(eventAccessRequestsView);
       }
       return eventAccessRequestsViews;
    }
}
