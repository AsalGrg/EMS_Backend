package com.backend.services;

import com.backend.dtos.AddStarringDto;
import com.backend.dtos.addEvent.EventStarringDetails;
import com.backend.models.Event;
import com.backend.models.EventStarring;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface StarringService {

    void saveStarring( EventStarringDetails eventStarringDetails, Event event);

    EventStarring getEventStarringByEventId(int eventId);
}
