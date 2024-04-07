package com.backend.serviceImpls;

import com.backend.exceptions.InternalServerError;
import com.backend.models.Event;
import com.backend.models.FavouriteEvents;
import com.backend.models.User;
import com.backend.repositories.FavouriteEventsRepository;
import com.backend.services.EventService;
import com.backend.services.FavouriteEventService;
import com.backend.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavouriteEventServiceImpl implements FavouriteEventService {

    private final FavouriteEventsRepository favouriteEventsRepository;
    private final UserService userService;

    public FavouriteEventServiceImpl(
            FavouriteEventsRepository favouriteEventsRepository,
            UserService userService
    ){
        this.favouriteEventsRepository= favouriteEventsRepository;
        this.userService= userService;
    }

    @Override
    public boolean checkIfHasLikedEvent(int userId, int eventId) {
        FavouriteEvents favouriteEvent= favouriteEventsRepository.getFavouriteEventByBothId(userId, eventId);
        return favouriteEvent!= null;
    }

    @Override
    public void addFavouriteEvent(Event event, int userId) {
        FavouriteEvents favouriteEvent = favouriteEventsRepository.getFavouriteEventByBothId(userId,event.getId());

        //checking if the vendor is followed by a user already or not, if yes throw an error.
        if(favouriteEvent!=null){
            throw new InternalServerError("Multiple event like requests");
        }

        favouriteEventsRepository.addFavouriteEvent(
                FavouriteEvents
                        .builder()
                        .event(event)
                        .user(userService.getUserByUserId(userId))
                        .build()
        );

    }

    @Override
    public void removeFavouriteEvent(int eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        favouriteEventsRepository.removeFavouriteEvent(user.getUserId(), eventId);
    }

    @Override
    public List<FavouriteEvents> getFavouriteEventsOfUser(int userId) {
        return favouriteEventsRepository.getFavouriteEventsOfUser(userId);
    }
}
