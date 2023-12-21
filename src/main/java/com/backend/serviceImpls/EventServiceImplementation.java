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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;

    private final EventTypeService eventTypeService;

    private final UserService userService;

    private final PromoCodeService promoCodeService;

    private final VendorCredentialService vendorCredentialsService;

    private final RoleService roleService;

    private final CloudinaryUploadService cloudinaryUploadServiceImpl;


    @Autowired
    public EventServiceImplementation

            (EventRepository eventRepository, EventTypeService eventTypeService, UserService userService
            , PromoCodeService promoCodeService,VendorCredentialService vendorCredentialsService,
             RoleService roleService, CloudinaryUploadService cloudinaryUploadServiceImpl){

        this.eventRepository= eventRepository;
        this.eventTypeService= eventTypeService;
        this.userService= userService;
        this.promoCodeService= promoCodeService;
        this.vendorCredentialsService= vendorCredentialsService;
        this.roleService= roleService;
        this.cloudinaryUploadServiceImpl= cloudinaryUploadServiceImpl;
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
    public void saveEvent(Event event){
        eventRepository.saveEvent(event);
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
    public EventResponseDto addEvent(AddEventRequestDto addEventDto) {

//        //checking if the event name already exists or not
//        boolean eventNameExists= eventRepository.existsByName(addEventDto.getName());
//
//        //if the event name matches
//        if(eventNameExists){
//            throw new ResourceAlreadyExistsException("Event name already exists");
//        }
//
//        VendorCredential vendorCredential= vendorCredentialsService.findVendorCredentialByUser(userService.getUserByUsername(addEventDto.getEvent_vendor()));
//
//        if(vendorCredential.isDeclined() && !vendorCredential.isVerified() && vendorCredential.isTerminated()){
//            throw new ResourceNotFoundException("Invalid vendor name");
//        }
//
//        if(addEventDto.getPromoCodes()!=null){
//
//            for(PromoCode promoCode: addEventDto.getPromoCodes()){
//                if(promoCodeService.checkPromoCodeExistsByTitle(promoCode.getName())){
//                    throw new ResourceAlreadyExistsException(promoCode.getName()+" already exits!");
//                }
//            }
//        }
//
//        String accessToken = null;
//        Set<User> usersInvited= new HashSet<>();
//
//        //checking if the event is private
//        if(addEventDto.isPrivate()){
//
//            //generating random access token for the event
//            accessToken = UUID.randomUUID().toString();
//
//            //checking if the user group is empty or not
//            if(addEventDto.getEvent_group()!=null){
//
//                List<String> user_groups= addEventDto.getEvent_group();
//
//                //event organizer and event_organizer is automatically added
//                user_groups.add(addEventDto.getEvent_organizer());
//                user_groups.add(addEventDto.getEvent_vendor());
//
//                System.out.println(user_groups);
//
//                for(String usernameOrEmail: user_groups){
//
//                    //checking each username provided in the user group set and if exists in the database
//                    User user= userService.getUserByUsernameOrEmail(usernameOrEmail);
//
//                    //adding the user details in the list
//                    usersInvited.add(user);
//                }
//            }
//        }
//
//        String coverImageUrl= null;
//        if(addEventDto.getEventCoverPhoto()!=null){
//            coverImageUrl = cloudinaryUploadServiceImpl.uploadImage(addEventDto.getEventCoverPhoto(), "Event Photos");
//        }
//
//
//        Event event= Event.builder()
//                .name(addEventDto.getName())
////                .eventDate(addEventDto.getEvent_date())
//                .eventType(eventTypeService.getEventTypeByTitle(addEventDto.getEventType()))
//                .isPrivate(addEventDto.isPrivate())
//                .seats(addEventDto.getSeats())
//                .eventOrganizer(userService.getUserByUsername(addEventDto.getEvent_organizer()))
//                .isAccepted(false)
//                .isDeclined(false)
//                .accessToken(accessToken)
////                .event_group(usersInvited)
//                .eventCoverImage(coverImageUrl)
//                .build();
//
//        //adding the event in the database
//        saveEvent(event);
//
//        if(addEventDto.getPromoCodes()!=null) {
//
//            for(PromoCode promoCode: addEventDto.getPromoCodes()) {
//                PromoCode savePromoCode = PromoCode
//                        .builder()
//                        .name(promoCode.getName())
//                        .discount_amount(promoCode.getDiscount_amount())
//                        .event(event)
//                        .build();
//
//                promoCodeService.savePromoCode(savePromoCode);
//            }
//
//        }

        return null;
    }

    @Override
    public void addPromoCode(AddPromoCodeDto promoCodeDto){
        promoCodeService.addPromocode(promoCodeDto, getEventByName(promoCodeDto.getName()));
    }

}
