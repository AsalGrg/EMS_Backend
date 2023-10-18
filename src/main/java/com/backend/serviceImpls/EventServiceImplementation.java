package com.backend.serviceImpls;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.AddEventResponseDto;
import com.backend.exceptions.InternalServerError;
import com.backend.exceptions.NotAuthorizedException;
import com.backend.exceptions.ResourceAlreadyExistsException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.models.Event;
import com.backend.models.PromoCode;
import com.backend.models.User;
import com.backend.repositories.EventRepository;
import com.backend.repositories.EventTypeRepository;
import com.backend.repositories.PromocodeRepository;
import com.backend.repositories.UserRepository;
import com.backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.*;


@Service
public class EventServiceImplementation implements EventService {

    private EventRepository eventRepository;

    private EventTypeRepository eventTypeRepository;

    private UserRepository userRepository;

    private PromocodeRepository promocodeRepository;

    @Autowired
    public EventServiceImplementation(EventRepository eventRepository, EventTypeRepository eventTypeRepository, UserRepository userRepository
    ,PromocodeRepository promocodeRepository){
        this.eventRepository= eventRepository;
        this.eventTypeRepository= eventTypeRepository;
        this.userRepository= userRepository;
        this.promocodeRepository= promocodeRepository;
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
        List<Event> events= this.eventRepository.findEventByLocation(place);

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
        List<Event> events = this.eventRepository.findAll();

        if(events.isEmpty()){
            throw new ResourceNotFoundException("Currently no available events");
        }

        return events;
    }


    //service method for adding new event
    @Override
    public AddEventResponseDto addEvent(AddEventRequestDto addEventDto) {

        //checking if the event name already exists or not
        boolean eventNameExists= this.eventRepository.existsByName(addEventDto.getName());

        if(addEventDto.getPromoCodes()!=null){

            for(PromoCode promoCode: addEventDto.getPromoCodes()){
                if(this.promocodeRepository.existsByName(promoCode.getName())){
                    throw new ResourceAlreadyExistsException(promoCode.getName()+" already exits!");
                }
            }
        }

        //if the event name matches
        if(eventNameExists){
            throw new ResourceAlreadyExistsException("Event name already exists");
        }


        //setting the event details from the dto to the event object
        Event event= new Event();
        event.setName(addEventDto.getName());
        event.setLocation(addEventDto.getLocation());
        event.setPublished_date(addEventDto.getPublished_date());
        event.setEvent_date(addEventDto.getEvent_date());
        event.setDescription(addEventDto.getDescription());
        event.setPrivate(addEventDto.isPrivate());
        event.setEntryFee(addEventDto.getEntryFee());
        event.setSeats(addEventDto.getSeats());
        event.setEvent_organizer(this.userRepository.findByUsername(addEventDto.getEvent_organizer()).orElseThrow(()->new ResourceNotFoundException("Invalid Event Organizer Data")));
        //checking if the event is private
        if(addEventDto.isPrivate()){

            //generating random access token for the event
            String accessToken = UUID.randomUUID().toString();
            event.setAccessToken(accessToken);


            //checking if the user group is empty or not
            if(addEventDto.getEvent_group()!=null){

                List<String> user_groups= addEventDto.getEvent_group();

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


        AddEventResponseDto addEventResponseDto = new AddEventResponseDto(event.getAccessToken(), event.getName(), event.getLocation(),event.getPublished_date(), event.getEvent_date(), event.getEntryFee());

        return addEventResponseDto;
    }

    @Override
    public AddEventResponseDto getEventByAccessToken(String accessToken, String username) {
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
                return new AddEventResponseDto(event.getAccessToken(),event.getName(),event.getLocation(),event.getPublished_date(),event.getEvent_date(),event.getEntryFee());
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

}
