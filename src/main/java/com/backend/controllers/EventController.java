package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessRequestsView;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.EventResponseDto;
import com.backend.models.Event;
import com.backend.serviceImpls.EventServiceImplementation;
import com.backend.serviceImpls.PromoCodeServiceImplementation;
import com.backend.utils.IsImage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@Validated
@CrossOrigin("*")
//@RequestMapping("/events")
public class EventController {

    private final EventServiceImplementation eventService;

    private final PromoCodeServiceImplementation promoCodeService;

    private final HttpSession httpSession;

    @Autowired
    public EventController
            (EventServiceImplementation eventService, HttpSession httpSession, @Lazy  PromoCodeServiceImplementation promoCodeService){
        this.eventService= eventService;
        this.httpSession= httpSession;
        this.promoCodeService= promoCodeService;
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

    @PostMapping("/private/{accessToken}")
    public ResponseEntity<?> getPrivateEventDetail(@PathVariable("accessToken") String accessToken){
        EventResponseDto eventDetails = eventService.enterEventByAccessToken(accessToken, (String) httpSession.getAttribute("CurrentUser"));

        return new ResponseEntity<>(eventDetails, HttpStatus.OK);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(@Valid @RequestPart("eventDetails") AddEventRequestDto addEventDto,
                                      @IsImage @RequestPart(value = "eventCoverPhoto" ,required = false)MultipartFile eventCoverPhoto){
//        addEventDto.setPublished_date(LocalDate.now());
        addEventDto.setEvent_organizer((String) httpSession.getAttribute("CurrentUser"));
        addEventDto.setEventCoverPhoto(eventCoverPhoto);


        return new ResponseEntity<>(eventService.addEvent(addEventDto), HttpStatus.OK);
    }

    @PostMapping("/addPromoCode")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddPromoCodeDto promoCodeDto){
        promoCodeDto.setUsername((String) httpSession.getAttribute("CurrentUser"));

        eventService.addPromoCode(promoCodeDto);

        return new ResponseEntity<>("Promo code Added Successfully",HttpStatus.OK);
    }


    @PostMapping("/event-access-request")
    public ResponseEntity<?> getEventAccessRequests(){
       List<EventAccessRequestsView> eventAccessRequestsViews=  eventService.getEventAccessRequests((String) httpSession.getAttribute("CurrentUser"));

       return new ResponseEntity<>(eventAccessRequestsViews, HttpStatus.OK);
    }

    @PostMapping("/make-event-access-request/{accessToken}")
    public ResponseEntity<?> sendEventAccessRequest(@PathVariable("accessToken") String accessToken){
        String currentUser= (String) httpSession.getAttribute("CurrentUser");

        eventService.makeEventAccessRequest(currentUser, accessToken);

        return new ResponseEntity<>("Your request has been collected", HttpStatus.OK);
    }
}
