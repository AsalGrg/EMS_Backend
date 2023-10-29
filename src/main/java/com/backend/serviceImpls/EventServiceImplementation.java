package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.exceptions.InternalServerError;
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

    private EventRepository eventRepository;

    private EventTypeRepository eventTypeRepository;

    private UserRepository userRepository;

    private PromocodeRepository promocodeRepository;

    private EventAccessRequestRepository eventAccessRequestRepo;

    private VendorCredentialsRepository vendorCredentialsRepo;

    private RoleRepository roleRepository;


    public EventResponseDto changeToEventDto(Event event){

        EventResponseDto eventResponseDto= new EventResponseDto(event.getAccessToken(), event.getName(), event.getLocation(), event.getPublished_date(), event.getEventDate(), event.getEntryFee());
        return eventResponseDto;

    }
    @Autowired
    public EventServiceImplementation
            (EventRepository eventRepository, EventTypeRepository eventTypeRepository, UserRepository userRepository
    ,PromocodeRepository promocodeRepository, EventAccessRequestRepository eventAccessRequestRepo, VendorCredentialsRepository vendorCredentialsRepository,
             RoleRepository roleRepository){
        this.eventRepository= eventRepository;
        this.eventTypeRepository= eventTypeRepository;
        this.userRepository= userRepository;
        this.promocodeRepository= promocodeRepository;
        this.eventAccessRequestRepo= eventAccessRequestRepo;
        this.vendorCredentialsRepo= vendorCredentialsRepository;
        this.roleRepository= roleRepository;
    }

    @Override
    public Event getEventById(int id) {
        Event event=  this.eventRepository.findEventById(id);
        if(event==null){
            throw new ResourceNotFoundException("Event with the given id does not exist");
        }

        return event;
    }

    @Override
    public List<Event> getEventByPlace(String place) {
        List<Event> events= this.eventRepository.findEventByLocationAndIsAcceptedAndIsDeclined(place, true, false);

        if (events.isEmpty()){
            throw new ResourceNotFoundException("Events for the given place are currently not available");
        }

        return events;
    }

    @Override
    public List<Event> getEventByType(String type) {
        return this.eventRepository.findEventByType(type);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = this.eventRepository.findByIsAcceptedAndIsDeclined(true, false);

        if(events.isEmpty()){
            throw new ResourceNotFoundException("Currently no available events");
        }

        return events;
    }

    //service method to get all the events from the filter such as time, date and place and venue
    public List<EventResponseDto> getEventsByFilter(SearchEventByFilterDto searchEventByFilterDto){
        List<Event> filterEventsList = this.eventRepository.findByLocationAndEventTimeAndEventDateAndEventType_TitleAndIsAccepted(
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
        List<Event> allTrendingEvents= this.eventRepository.findAllByIsAcceptedAndEventDateAfterOrderByTicketSoldDesc(true, LocalDate.now());

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
        boolean eventNameExists= this.eventRepository.existsByName(addEventDto.getName());

        //if the event name matches
        if(eventNameExists){
            throw new ResourceAlreadyExistsException("Event name already exists");
        }

        VendorCredential vendorCredential= vendorCredentialsRepo.findByUser(this.userRepository.findByUsername(addEventDto.getEvent_vendor()).
                orElseThrow(()->new ResourceNotFoundException("Invalid vendor name")))
                .orElseThrow(()->new ResourceNotFoundException("Invalid vendor name"));

        if(vendorCredential.isDeclined() && !vendorCredential.isVerified() && vendorCredential.isTerminated()){
            throw new ResourceNotFoundException("Invalid vendor name");
        }



        if(addEventDto.getPromoCodes()!=null){

            for(PromoCode promoCode: addEventDto.getPromoCodes()){
                if(this.promocodeRepository.existsByName(promoCode.getName())){
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
        event.setEvent_organizer(this.userRepository.findByUsername(addEventDto.getEvent_organizer()).orElseThrow(()->new ResourceNotFoundException("Invalid Event Organizer Data")));

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

                for(String username: user_groups){

                    //checking each username provided in the user group set and if exists in the database
                    User user= this.userRepository.findByEmailOrUsername(username, username)
                           //throwing exception if the username does not exist
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(username+ " is not found, or yet registered")
                            );

                    //adding the user details in the list
                    if(!usersInvited.contains(user)) usersInvited.add(user);
                }
                //adding the list of the user group in the event object
                event.setEvent_group(usersInvited);
            }
        }

        //other entities such as event group are to be set
        event.setEventType(this.eventTypeRepository.getEventTypeByTitle(addEventDto.getEventType()));

        //adding the event in the database
        this.eventRepository.save(event);

        if(addEventDto.getPromoCodes()!=null) {

            for(PromoCode promoCode: addEventDto.getPromoCodes()) {
                PromoCode savePromoCode = new PromoCode();
                savePromoCode.setName(promoCode.getName());
                savePromoCode.setDiscount_amount(promoCode.getDiscount_amount());
                savePromoCode.setEvent(event);

                this.promocodeRepository.save(savePromoCode);
            }

        }


        return changeToEventDto(event);
    }

    //method for vendor to accept the addEvent requests from the client i.e. event hoster
    public void addEventVendorRequestAction(String username, String action, String eventName){
        User user= this.userRepository.findByUsername(username).get();

        Event eventDetails= this.eventRepository.findEventByName(eventName).orElseThrow(()->
                new ResourceNotFoundException("Invalid Event name"));

        if(user.getUserRoles().contains(this.roleRepository.findByTitle("VENDOR").get())
                &&
                !user.getUsername().equals(eventDetails.getEvent_vendor().getUsername())){

            throw new NotAuthorizedException("Access Denied: You do not have the necessary permissions to perform this action.");
        }

        switch (action){

            case "accept" -> {
                eventDetails.setAccepted(true);
                this.eventRepository.save(eventDetails);
            }

            case "reject" ->{
                eventDetails.setDeclined(true);
                this.eventRepository.save(eventDetails);
            }
        }


    }

    @Override
    public EventResponseDto getEventByAccessToken(String accessToken, String username) {
        if(username==null){
            throw new NotAuthorizedException();
        }

        Event event= this.eventRepository.findByAccessToken(accessToken)
                .orElseThrow(
                        ()->{
                            throw new ResourceNotFoundException("Invalid Access Token");
                        }
                );

        Set<User> usersInvited= event.getEvent_group();

        for(User user: usersInvited){
            if (user.getUsername().equals(username)){
                return changeToEventDto(event);
            }
        }

        throw new NotAuthorizedException("You are not allowed to access the event");
    }


    @Override
    public PromoCode addPromocode(AddPromoCodeDto promoCodeDto) {

        Event event = this.eventRepository.findEventByName(promoCodeDto.getEvent_name())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Event with title " + promoCodeDto.getEvent_name()+ " does not exist")
                );

        if(!event.getEvent_organizer().getUsername().equals(promoCodeDto.getUsername())){
            throw new NotAuthorizedException("You do not have privileges to add promo codes to the event");
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setName(promoCodeDto.getName());
        promoCode.setDiscount_amount(promoCodeDto.getDiscount_amount());
        promoCode.setEvent(event);

        PromoCode savedPromocode = this.promocodeRepository.save(promoCode);

        if(savedPromocode.getId()==null){

            //should throw exception ...to be continued
            throw new InternalServerError();
        }
        return savedPromocode;
    }


    //service method to make event access request
    public void makeEventAccessRequest(String username, String accessToken){

        EventAccessRequest requestExists = this.eventAccessRequestRepo.findByUserAndEvent(
                this.userRepository.findByUsername(username).get(),
                this.eventRepository.findByAccessToken(accessToken).get()
        );

        if(requestExists!=null){
            throw new ResourceAlreadyExistsException("Request has already been collected");
        }

        requestExists= new EventAccessRequest();
        requestExists.setUser(this.userRepository.findByUsername(username).get());
        requestExists.setEvent(this.eventRepository.findByAccessToken(accessToken).get());

        this.eventAccessRequestRepo.save(requestExists);

    }

    //service method handler for getting the event access requests
    @Override
    public List<EventAccessRequestsView> getEventAccessRequests(String username) {

       List<EventAccessRequest> allEventAccessRequests=  this.eventAccessRequestRepo.findAll();

       List<EventAccessRequest> filteredEventAccessRequests = new ArrayList<>();
       for(EventAccessRequest eventAccessRequest: allEventAccessRequests){

           if(eventAccessRequest.getEvent().getEvent_organizer().getUsername().equals(username)){
               filteredEventAccessRequests.add(eventAccessRequest);
           }
       }

       if(filteredEventAccessRequests.isEmpty()){
           throw new ResourceNotFoundException("No requests at the moment");
       }

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
