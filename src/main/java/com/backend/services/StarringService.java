package com.backend.services;

import com.backend.dtos.AddStarringDto;
import com.backend.models.Event;
import com.backend.models.EventStarring;

import java.util.List;

public interface StarringService {

    void saveStarring(List<AddStarringDto> starrings, Event event);
}
