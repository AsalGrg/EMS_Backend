package com.backend.services;

import com.backend.dtos.AddPromoCodeDto;
import com.backend.dtos.addEvent.AddEventRequestDto;
import com.backend.dtos.addEvent.AddEventResponseDto;
import com.backend.models.Event;
import com.backend.models.PromoCode;

import java.util.List;

public interface EventService{

    public Event getEventById(int id);

    public List<Event> getEventByPlace(String place);

    public List<Event> getEventByType(String type);

    public List<Event> getAllEvents();

    public AddEventResponseDto addEvent(AddEventRequestDto addEventDto);

    public AddEventResponseDto getEventByAccessToken(String accessToken, String username);

    public PromoCode addPromocode(AddPromoCodeDto promoCodeDto);
}
