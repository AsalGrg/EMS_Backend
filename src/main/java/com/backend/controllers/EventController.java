package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.AddStarringDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.*;
import com.backend.models.Event;
import com.backend.services.EventService;
import com.backend.utils.IsImage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@RestController
@Validated
@CrossOrigin("*")

@Slf4j
//@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    private final HttpSession httpSession;

    @Autowired
    public EventController
            (EventService eventService, HttpSession httpSession){
        this.eventService= eventService;
        this.httpSession= httpSession;
    }


    @GetMapping("/allEvents")
    public ResponseEntity<List<Event>> getAllEvents(){
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/event_id/{id}")
    public ResponseEntity<Event> getEventDetailsById(@PathVariable("id") int id){
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/place/{place}")
    public ResponseEntity<List<Event>> getEventByPlace(@PathVariable("place") String place){
        return new ResponseEntity<>(eventService.getEventByPlace(place), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<Event>> getEventByType(@PathVariable("type")String type){
        return new ResponseEntity<>(eventService.getEventByType(type), HttpStatus.OK);
    }


    @PostMapping ("/eventByFilter")
    public ResponseEntity<?> getEventsByFilters(@Valid @RequestBody SearchEventByFilterDto searchEventByFilterDto){
        List<EventResponseDto> filteredEvents = eventService.getEventsByFilter(searchEventByFilterDto);

        return new ResponseEntity<>(filteredEvents, HttpStatus.OK);
    }

    @GetMapping("/trendingEvents")
    public ResponseEntity<?> getTrendingEvents(){
        List<EventResponseDto> trendingEvents = eventService.getTrendingEvents();

        return ResponseEntity.ok(trendingEvents);
    }

    @PostMapping( path = "/addEvent"
            , consumes = {"multipart/form-data"})
    public ResponseEntity<?> addEvent(@Valid @RequestPart(name = "eventDetails") AddEventRequestDto addEventDto,
                                      @Valid @RequestPart(name = "eventDateDetails")EventDateDetailsDto eventDateDetailsDto,
                                      @IsImage @RequestPart(name = "eventCoverImage") MultipartFile eventCoverImage,
                                      @Valid @RequestPart(name = "eventTicketDetails")EventTicketDetailsDto eventTicketDetailsDto,
                                      @Valid @RequestPart(name = "eventStarringDetails", required = false) EventStarringDetails eventStarringDetails,
                                      @RequestPart(name = "starring1Photo", required = false) MultipartFile starring1Photo,
                                      @RequestPart(name = "starring2Photo", required = false) MultipartFile starring2Photo,
                                      @RequestPart(name = "starring3Photo", required = false) MultipartFile starring3Photo,
                                      @RequestPart(name = "starring4Photo", required = false) MultipartFile starring4Photo,
                                      @RequestPart(name = "starring5Photo", required = false) MultipartFile starring5Photo
                                      ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        addEventDto.setEventCoverPhoto(eventCoverImage);

//        return new ResponseEntity<>(eventService.addEvent(addEventDto,eventTicketDetailsDto, eventDateDetailsDto), HttpStatus.OK);
        MultipartFile[] multipartFiles= {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};
        for (int i =0; i<multipartFiles.length; i++){
            if(multipartFiles[i]!=null){
                 Method method= eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", MultipartFile.class);
                method.invoke(eventStarringDetails, multipartFiles[i]);
            }
        }
        return new ResponseEntity<>(eventService.addEvent(addEventDto,eventTicketDetailsDto, eventDateDetailsDto, eventStarringDetails), HttpStatus.OK);
//        log.info("dsldld"+ eventStarringDetails.getStarring1Name());
//        return  ResponseEntity.ok("");
    }
    @PostMapping( path = "/check"
    ,consumes = {"multipart/form-data"})
    public ResponseEntity<?> check(@Valid @RequestPart("eventDetails") AddEventRequestDto addEventDto,
                                   @Valid @RequestPart(name = "eventDateDetails")EventDateDetailsDto eventDateDetailsDto,
                                   @Valid @RequestPart(name = "eventTicketDetails")EventTicketDetailsDto eventTicketDetailsDto){
//        addEventDto.setPublished_date(LocalDate.now());
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

//
//    @PostMapping( path = "/addEvent"
//            , consumes = {"multipart/form-data"})
//    public ResponseEntity<?> addEvent(@Valid @ModelAttribute AddEventRequestDto addEventDto){
////        addEventDto.setPublished_date(LocalDate.now());
//        return new ResponseEntity<>(eventService.addEvent(addEventDto), HttpStatus.OK);
//    }

    @PostMapping("/addPromoCode")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddPromoCodeDto promoCodeDto){
        promoCodeDto.setUsername((String) httpSession.getAttribute("CurrentUser"));

        eventService.addPromoCode(promoCodeDto);

        return new ResponseEntity<>("Promo code Added Successfully",HttpStatus.OK);
    }


//    @PostMapping("/event-access-request")
//    public ResponseEntity<?> getEventAccessRequests(){
//       List<EventAccessRequestsView> eventAccessRequestsViews=  eventService.getEventAccessRequests((String) httpSession.getAttribute("CurrentUser"));
//
//       return new ResponseEntity<>(eventAccessRequestsViews, HttpStatus.OK);
//    }

//    @PostMapping("/make-event-access-request/{accessToken}")
//    public ResponseEntity<?> sendEventAccessRequest(@PathVariable("accessToken") String accessToken){
//        String currentUser= (String) httpSession.getAttribute("CurrentUser");
//
//        eventService.makeEventAccessRequest(currentUser, accessToken);
//
//        return new ResponseEntity<>("Your request has been collected", HttpStatus.OK);
//    }
}
