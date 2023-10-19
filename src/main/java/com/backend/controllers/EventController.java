package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.AddEventResponseDto;
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

    @Autowired
    public EventController(EventServiceImplementation eventService){
        this.eventService= eventService;
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

    @PostMapping("/private/{accessToken}")
    public ResponseEntity<?> getPrivateEventDetail(@PathVariable("accessToken") String accessToken, HttpSession httpSession){
        String username= (String)httpSession.getAttribute("CurrentUser");
        AddEventResponseDto eventDetails = this.eventService.getEventByAccessToken(accessToken, username);

        return new ResponseEntity<>(eventDetails, HttpStatus.OK);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddEventRequestDto addEventDto, HttpSession httpSession){
        addEventDto.setPublished_date(LocalDate.now());
        addEventDto.setEvent_organizer((String) httpSession.getAttribute("CurrentUser"));

        return new ResponseEntity<>(this.eventService.addEvent(addEventDto), HttpStatus.OK);
    }

    @PostMapping("/addPromoCode")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddPromoCodeDto promoCodeDto, HttpSession httpSession){
        promoCodeDto.setUsername((String) httpSession.getAttribute("CurrentUser"));
        this.eventService.addPromocode(promoCodeDto);

        return new ResponseEntity<>("Promocode Added Successfully",HttpStatus.OK);
    }
}
