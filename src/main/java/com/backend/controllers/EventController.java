package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.models.Event;
import com.backend.serviceImpls.EventServiceImplementation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
//@RequestMapping("/events")
public class EventController {

    private EventServiceImplementation eventService;

    private HttpSession httpSession;

    @Autowired
    public EventController(EventServiceImplementation eventService, HttpSession httpSession){
        this.eventService= eventService;
        this.httpSession= httpSession;
    }


    @GetMapping("/")
    public ResponseEntity<List<Event>> getAllEvents(){
        return new ResponseEntity<>(this.eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/event_id/{id}")
    public ResponseEntity<Event> getEventDetailsById(@PathVariable("id") int id){
        return new ResponseEntity<>(this.eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/place/{place}")
    public ResponseEntity<List<Event>> getEventByPlace(@PathVariable("place") String place){
        return new ResponseEntity<>(this.eventService.getEventByPlace(place), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<Event>> getEventByType(@PathVariable("type")String type){
        return new ResponseEntity<>(this.eventService.getEventByType(type), HttpStatus.OK);
    }


    @PostMapping ("/eventByFilter")
    public ResponseEntity<?> getEventsByFilters(@Valid @RequestBody SearchEventByFilterDto searchEventByFilterDto){
        List<EventResponseDto> filteredEvents = this.eventService.getEventsByFilter(searchEventByFilterDto);

        return new ResponseEntity<>(filteredEvents, HttpStatus.OK);
    }

    @GetMapping("/trendingEvents")
    public ResponseEntity<?> getTrendingEvents(){
        List<EventResponseDto> trendingEvents = this.eventService.getTrendingEvents();

        return ResponseEntity.ok(trendingEvents);
    }

    @PostMapping("/private/{accessToken}")
    public ResponseEntity<?> getPrivateEventDetail(@PathVariable("accessToken") String accessToken){
        EventResponseDto eventDetails = this.eventService.getEventByAccessToken(accessToken, (String) httpSession.getAttribute("CurrentUser"));

        return new ResponseEntity<>(eventDetails, HttpStatus.OK);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddEventRequestDto addEventDto){
        addEventDto.setPublished_date(LocalDate.now());
        addEventDto.setEvent_organizer((String) httpSession.getAttribute("CurrentUser"));

        return new ResponseEntity<>(this.eventService.addEvent(addEventDto), HttpStatus.OK);
    }

    @PostMapping("/addPromoCode")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddPromoCodeDto promoCodeDto){
        promoCodeDto.setUsername((String) httpSession.getAttribute("CurrentUser"));
        this.eventService.addPromocode(promoCodeDto);

        return new ResponseEntity<>("Promo code Added Successfully",HttpStatus.OK);
    }


    @PostMapping("/event-access-request")
    public ResponseEntity<?> getEventAccessRequests(){
       List<EventAccessRequestsView> eventAccessRequestsViews=  this.eventService.getEventAccessRequests((String) httpSession.getAttribute("CurrentUser"));

       return new ResponseEntity<>(eventAccessRequestsViews, HttpStatus.OK);
    }

    @PostMapping("/make-event-access-request/{accessToken}")
    public ResponseEntity<?> sendEventAccessRequest(@PathVariable("accessToken") String accessToken){
        String currentUser= (String) httpSession.getAttribute("CurrentUser");

        this.eventService.makeEventAccessRequest(currentUser, accessToken);

        return new ResponseEntity<>("Your request has been collected", HttpStatus.OK);
    }
}
