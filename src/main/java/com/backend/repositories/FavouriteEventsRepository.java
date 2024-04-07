package com.backend.repositories;

import com.backend.models.FavouriteEvents;
import com.backend.models.VendorFollowers;

import java.util.List;

public interface FavouriteEventsRepository {

    void addFavouriteEvent(FavouriteEvents favouriteEvent);
    void removeFavouriteEvent(int userId, int eventId);
    List<FavouriteEvents> getFavouriteEventsOfUser(int userId);
    FavouriteEvents getFavouriteEventByBothId(int userId, int eventId);
}
