package com.backend.services;

import com.backend.models.Event;
import com.backend.models.FavouriteEvents;

import java.util.List;

public interface FavouriteEventService {
    boolean checkIfHasLikedEvent(int userId, int eventId);
    void addFavouriteEvent(Event event, int userId);
    void removeFavouriteEvent (int eventId);
    List<FavouriteEvents> getFavouriteEventsOfUser(int userId);
}
