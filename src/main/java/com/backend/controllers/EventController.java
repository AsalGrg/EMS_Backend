package com.backend.controllers;


import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.EventAccessDetails;
import com.backend.dtos.EventCollectionSnippet;
import com.backend.dtos.SearchEventByFilterDto;
import com.backend.dtos.addEvent.*;
import com.backend.models.Event;
import com.backend.models.EventPhysicalLocationDetails;
import com.backend.services.EventCategoryService;
import com.backend.services.EventService;
import com.backend.services.FavouriteEventService;
import com.backend.utils.IsImage;
import jakarta.servlet.http.HttpServletRequest;
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
    private final EventCategoryService eventCategoryService;
    private final FavouriteEventService favouriteEventService;


    @Autowired
    public EventController
            (EventService eventService,
             EventCategoryService eventCategoryService,
             FavouriteEventService favouriteEventService
             ){
        this.eventService= eventService;
        this.eventCategoryService= eventCategoryService;
        this.favouriteEventService= favouriteEventService;
    }


    @GetMapping("/allEvents")
    public ResponseEntity<List<Event>> getAllEvents(){
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/getAllVendorEvents")
    public ResponseEntity<?> getAllVendorEvents(){
        return ResponseEntity.ok(eventService.getAllVendorEventsSnippets());
    }

    @GetMapping("/getEventInternalDescription/{eventId}")
    public ResponseEntity<?> getEventInternalDetails(@PathVariable("eventId") int eventId){
        return new ResponseEntity<>(eventService.getEventInternalDetails(eventId), HttpStatus.OK);
    }

    @GetMapping("/getAllVendorEventOrders")
    public ResponseEntity<?> getAllVendorEventsOrder(){
        return new ResponseEntity<>(eventService.getAllVendorOrders(), HttpStatus.OK);
    }

    @PostMapping("/event_id/{id}")
    public ResponseEntity<?> getEventDetailsById(@PathVariable("id") int id, @RequestPart(name = "eventAccessDetails",required = false)EventAccessDetails eventAccessDetails, HttpServletRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        log.info("Herrre");
        if(eventAccessDetails!=null){
            log.info(String.valueOf(id));
            log.info(eventAccessDetails.getAccessPassword());
        }
        return new ResponseEntity<>(eventService.getAboutEventByEventId(id, request, eventAccessDetails), HttpStatus.OK);
    }

    @GetMapping("/draft/event_id/{id}")
    public ResponseEntity<?> getEventDraftDetails(@PathVariable("id") int id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(eventService.getEventDraftDetails(id), HttpStatus.OK);
    }

    @GetMapping("/edit/event_id/{id}")
    public ResponseEntity<?> getEventEditDetails(@PathVariable("id") int id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(eventService.getEventEditDetails(id), HttpStatus.OK);
    }


    @GetMapping("/place/{place}")
    public ResponseEntity<?> getEventByPlace(@PathVariable("place") String place){
        log.info(place);
        return new ResponseEntity<>(eventService.getEventByPlace(place), HttpStatus.OK);
    }

    //the request is because if the user has token just in case so that we can track the vendors followed
    @GetMapping("/event/{type}/{location}")
    public ResponseEntity<?> getEventByType(@PathVariable("type")String type, @PathVariable("location") String location,
                                            HttpServletRequest request
                                            ){
        return new ResponseEntity<>(eventService.getEventByTypeAndLocation(type, location, request), HttpStatus.OK);
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
        log.info("Keyword: ", keyword);
        List<EventResponseDto> searchedEvents= eventService.getQuickSearchResult(keyword);
        return new ResponseEntity<>(searchedEvents, HttpStatus.OK);
    }

    @GetMapping("/likeEvent/{eventId}")
    public ResponseEntity<?> likeEvent(@PathVariable(name = "eventId") int eventId){
        eventService.likeEvent(eventId);
        return ResponseEntity.ok("Event liked successfully");
    }

    @GetMapping("/unlikeEvent/{eventId}")
    public ResponseEntity<?> unlikeEvent(@PathVariable(name = "eventId") int eventId){
        favouriteEventService.removeFavouriteEvent(eventId);
        return ResponseEntity.ok("Event unliked successfully");
    }

    @GetMapping("/getAllLikedEvents")
    public ResponseEntity<?> getAllLikedEvents(){
        return ResponseEntity.ok(eventService.getAllUserLikedEvents());
    }

    @GetMapping("/trendingEvents")
    public ResponseEntity<?> getTrendingEvents(){
        List<EventResponseDto> trendingEvents = eventService.getTrendingEvents();

        return ResponseEntity.ok(trendingEvents);
    }

    @GetMapping("/allCategories")
    public ResponseEntity<?> getAllEventCategories(){
        return ResponseEntity.ok(eventCategoryService.getAllEventCategories());
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
            @RequestPart(name = "eventCoverImage", required = false) MultipartFile eventCoverImage,
            @Valid @RequestPart(name = "eventStarringDetails", required = false) EventStarringDetails eventStarringDetails,
            @RequestPart(name = "starring1Photo", required = false) MultipartFile starring1Photo,
            @RequestPart(name = "starring2Photo", required = false) MultipartFile starring2Photo,
            @RequestPart(name = "starring3Photo", required = false) MultipartFile starring3Photo,
            @RequestPart(name = "starring4Photo", required = false) MultipartFile starring4Photo,
            @RequestPart(name = "starring5Photo", required = false) MultipartFile starring5Photo
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if(addEventSecondPageDto.getEventCoverImage()==null){
            addEventSecondPageDto.setEventCoverImage(eventCoverImage);
        }

        MultipartFile[] multipartFiles = {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};

        if(eventStarringDetails!=null){
            for (int i= 0; i < multipartFiles.length; i++) {
                Method getMethod = eventStarringDetails.getClass().getMethod("getStarring" + (i + 1) + "Photo");
                Object starringPhotoLink = getMethod.invoke(eventStarringDetails);

                if (starringPhotoLink != null) continue;

                if (multipartFiles[i] != null) {

                    Method method = eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", Object.class);
                    method.invoke(eventStarringDetails, multipartFiles[i]);
                }
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

        if(addEventSecondPageDto.getEventCoverImage()==null){
            addEventSecondPageDto.setEventCoverImage(eventCoverImage);
        }

        MultipartFile[] multipartFiles = {starring1Photo, starring2Photo, starring3Photo, starring4Photo, starring5Photo};

        if(eventStarringDetails!=null){
            for (int i= 0; i < multipartFiles.length; i++) {
                Method getMethod = eventStarringDetails.getClass().getMethod("getStarring" + (i + 1) + "Photo");
                Object starringPhotoLink = getMethod.invoke(eventStarringDetails);

                if (starringPhotoLink != null) continue;

                if (multipartFiles[i] != null) {

                    Method method = eventStarringDetails.getClass().getMethod("setStarring" + (i + 1) + "Photo", Object.class);
                    method.invoke(eventStarringDetails, multipartFiles[i]);
                }
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
    public ResponseEntity<?> addEvent(@Valid @RequestPart("promoCodeDetails") AddPromoCodeDto promoCodeDto) {

        eventService.addPromoCode(promoCodeDto);

        return new ResponseEntity<>("Promo code Added Successfully", HttpStatus.OK);
    }

    @PostMapping("/addCollection")
    public ResponseEntity<?> addCollection (@IsImage @RequestPart("collectionCoverImg") MultipartFile coverImage, @RequestPart("eventCollectionDetails") EventCollectionSnippet eventCollectionSnippet){
        eventCollectionSnippet.setCoverImg(coverImage);
        eventService.addEventCollection(eventCollectionSnippet);
        return new ResponseEntity<>("Event collection added successfully", HttpStatus.OK);
    }

    @GetMapping("/getAllCollections")
    public ResponseEntity<?>  getAllCollections (){
        return new ResponseEntity<>(eventService.getAllEventCollections(), HttpStatus.OK);
    }

    @GetMapping("/getAllEventRequests")
    public ResponseEntity<?> getAllEventRequests(){
        return new ResponseEntity<>(eventService.getAllEventRequests(), HttpStatus.OK);
    }

    @GetMapping("/updateEventRequest/{eventId}/{action}")
    public ResponseEntity<?> getAllEventRequests(@PathVariable(name = "eventId") int eventId, @PathVariable(name = "action") String action){
        eventService.updateEventRequest(eventId, action);
        return new ResponseEntity<>( HttpStatus.OK);

    }
}
