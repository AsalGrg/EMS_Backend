package com.backend.controllers;


import com.backend.dtos.AddEventDto;
import com.backend.models.Event;
import com.backend.serviceImpls.EventServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
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

    @PostMapping("/addEvent")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody AddEventDto addEventDto){
        addEventDto.setPublishedDate(LocalDate.now());
        return new ResponseEntity<>(this.eventService.addEvent(addEventDto), HttpStatus.OK);
    }
}
