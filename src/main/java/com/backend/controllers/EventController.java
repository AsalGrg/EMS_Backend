package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.*;
import com.backend.models.Event;
import com.backend.models.EventPhysicalLocationDetails;
import com.backend.services.EventService;
import com.backend.utils.IsImage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

    @GetMapping("/search/{eventTitle}/{eventVenue}")
    public ResponseEntity<?> getEventsBySearch (@PathVariable("eventTitle") String eventTitle, @PathVariable("eventVenue") String eventVenue){

        List<EventResponseDto> searchedEvents= eventService.getEventsBySearch(eventTitle, eventVenue);
        return new ResponseEntity<>(searchedEvents, HttpStatus.OK);
    }

    @GetMapping("/search/quickSearch/{keyword}")
    public ResponseEntity<?> getQuickSearchResults(@PathVariable("keyword") String keyword){
        List<EventResponseDto> searchedEvents= eventService.getQuickSearchResult(keyword);
        return new ResponseEntity<>(searchedEvents, HttpStatus.OK);
    }

    @GetMapping("/trendingEvents")
    public ResponseEntity<?> getTrendingEvents(){
        List<EventResponseDto> trendingEvents = eventService.getTrendingEvents();

        return ResponseEntity.ok(trendingEvents);
    }


    @PostMapping("/addFirstPageInfo")
    public ResponseEntity<?> addFirstPageInfo(@Valid @RequestPart (name = "eventFirstPageDetails") AddEventFirstPageDto addEventFirstPageDto,
                                              @Valid @RequestPart(name = "eventDateDetails")EventDateDetailsDto eventDateDetailsDto,
                                              @Valid @RequestPart(name = "eventPhysicalLocationDetails", required = false) EventPhysicalLocationDetailsDto eventPhysicalLocationDetails
                                              ){
        Integer eventId = eventService.addFirstPageDetails(addEventFirstPageDto,eventDateDetailsDto, eventPhysicalLocationDetails);
        log.info("Event IDsdlsldsldsld: "+ eventId);
        return new ResponseEntity<>(eventId, HttpStatus.OK);
    }

    @PostMapping("/addSecondPageInfo")
    public ResponseEntity<?> addSecondPageInfo (
            @Valid @RequestPart(name = "eventSecondPageDetails") AddEventSecondPageDto addEventSecondPageDto,
            @IsImage @RequestPart(name = "eventCoverImage") MultipartFile eventCoverImage,
            @Valid @RequestPart(name = "eventStarringDetails", required = false) EventStarringDetails eventStarringDetails,
            @RequestPart(name = "starring1Photo", required = false) MultipartFile starring1Photo,
            @RequestPart(name = "starring2Photo", required = false) MultipartFile starring2Photo,
            @RequestPart(name = "starring3Photo", required = false) MultipartFile starring3Photo,
            @RequestPart(name = "starring4Photo", required = false) MultipartFile starring4Photo,
            @RequestPart(name = "starring5Photo", required = false) MultipartFile starring5Photo
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        addEventSecondPageDto.setEventCoverImage(eventCoverImage);

        MultipartFile[] multipartFiles = {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};
        for (int i = 0; i < multipartFiles.length; i++) {
            if (multipartFiles[i] != null) {
                Method method = eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", MultipartFile.class);
                method.invoke(eventStarringDetails, multipartFiles[i]);
            }
        }

        eventService.addEventSecondPageDetails(addEventSecondPageDto, eventStarringDetails);

        return ResponseEntity.ok("Second Page Details added successfully");
    }

    @PostMapping("/draftSecondPageInfo")
    public ResponseEntity<?> saveSecondPageDraft (
            @RequestPart(name = "eventSecondPageDetails") AddEventSecondPageDto addEventSecondPageDto,
            @RequestPart(name = "eventCoverImage", required = false) MultipartFile eventCoverImage,
            @RequestPart(name = "eventStarringDetails", required = false) EventStarringDetails eventStarringDetails,
            @RequestPart(name = "starring1Photo", required = false) MultipartFile starring1Photo,
            @RequestPart(name = "starring2Photo", required = false) MultipartFile starring2Photo,
            @RequestPart(name = "starring3Photo", required = false) MultipartFile starring3Photo,
            @RequestPart(name = "starring4Photo", required = false) MultipartFile starring4Photo,
            @RequestPart(name = "starring5Photo", required = false) MultipartFile starring5Photo
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        addEventSecondPageDto.setEventCoverImage(eventCoverImage);
        MultipartFile[] multipartFiles = {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};
        for (int i = 0; i < multipartFiles.length; i++) {
            if (multipartFiles[i] != null) {
                Method method = eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", MultipartFile.class);
                method.invoke(eventStarringDetails, multipartFiles[i]);
            }
        }
        eventService.addEventSecondPageDetails(addEventSecondPageDto, eventStarringDetails);

        return ResponseEntity.ok("Second Page Details added successfully");
    }


    @PostMapping("/addThirdPageDetails")
    public ResponseEntity<?> addThirdPageInfo(@Valid @RequestPart(name = "eventTicketDetails")EventTicketDetailsDto eventTicketDetailsDto){
        eventService.addEventThirdPageDetails(eventTicketDetailsDto);
        return ResponseEntity.ok("Third Page Details added successfully");
    }

    @PostMapping("/draftThirdPageInfo")
    public ResponseEntity<?> saveSecondPageDraft(@RequestPart(name = "eventTicketDetails")EventTicketDetailsDto eventTicketDetailsDto){
        eventService.addEventThirdPageDetails(eventTicketDetailsDto);
        return ResponseEntity.ok("Third Page Details added successfully");
    }

    @PostMapping("/addFourthPageDetails")
    public ResponseEntity<?> addFourthPageDetails(@Valid @RequestPart(name = "eventFourthPageDetails") AddEventFourthPageDto addEventFourthPageDto){
        eventService.addEventFourthPageDetails(addEventFourthPageDto);
        return ResponseEntity.ok("Fourth Page Details added successfully");
    }

    @PostMapping("/draftFourthPageDetails")
    public ResponseEntity<?> draftFourthPageDetails(@RequestPart(name = "eventFourthPageDetails") AddEventFourthPageDto addEventFourthPageDto){
        eventService.addEventFourthPageDetails(addEventFourthPageDto);
        return ResponseEntity.ok("Fourth Page Details added successfully");
    }

//
//    //for adding event at once
//    @PostMapping( path = "/addEvent"
//            , consumes = {"multipart/form-data"})
//    public ResponseEntity<?> addEvent(@Valid @RequestPart(name = "eventDetails") AddEventRequestDto addEventDto,
//                                      @IsImage @RequestPart(name = "eventCoverImage") MultipartFile eventCoverImage,
//                                      @Valid @RequestPart(name = "eventTicketDetails")EventTicketDetailsDto eventTicketDetailsDto,
//                                      @Valid @RequestPart(name = "eventStarringDetails", required = false) EventStarringDetails eventStarringDetails,
//                                      @RequestPart(name = "starring1Photo", required = false) MultipartFile starring1Photo,
//                                      @RequestPart(name = "starring2Photo", required = false) MultipartFile starring2Photo,
//                                      @RequestPart(name = "starring3Photo", required = false) MultipartFile starring3Photo,
//                                      @RequestPart(name = "starring4Photo", required = false) MultipartFile starring4Photo,
//                                      @RequestPart(name = "starring5Photo", required = false) MultipartFile starring5Photo
//                                      ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//
//        addEventDto.setEventCoverPhoto(eventCoverImage);
//
////        return new ResponseEntity<>(eventService.addEvent(addEventDto,eventTicketDetailsDto, eventDateDetailsDto), HttpStatus.OK);
//        MultipartFile[] multipartFiles = {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};
//        for (int i = 0; i < multipartFiles.length; i++) {
//            if (multipartFiles[i] != null) {
//                Method method = eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", MultipartFile.class);
//                method.invoke(eventStarringDetails, multipartFiles[i]);
//            }
//        }
//        return new ResponseEntity<>(eventService.addEvent(addEventDto, eventTicketDetailsDto, eventDateDetailsDto, eventStarringDetails,eventPhysicalLocationDetails), HttpStatus.OK);
//    }
//

    @PostMapping("/addPromoCode")
    public ResponseEntity<?> addEvent(@Valid @RequestBody AddPromoCodeDto promoCodeDto) {
        promoCodeDto.setUsername((String) httpSession.getAttribute("CurrentUser"));

        eventService.addPromoCode(promoCodeDto);

        return new ResponseEntity<>("Promo code Added Successfully", HttpStatus.OK);
    }
}
