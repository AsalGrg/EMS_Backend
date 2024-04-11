package com.backend.dtos.user;

import com.backend.dtos.EditProfileDetails;
import com.backend.dtos.addEvent.EventResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor

public class UserProfileDetailsResponse {

    private EditProfileDetails userSnippetDetails;

    private int noOfEvents;

    private List<EventResponseDto> pastEvents;

    private List<EventResponseDto> upcomingEvents;

    private boolean isUserProfile;
}
