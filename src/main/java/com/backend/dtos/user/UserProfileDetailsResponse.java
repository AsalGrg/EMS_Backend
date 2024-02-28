package com.backend.dtos.user;

import com.backend.dtos.addEvent.EventResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor

public class UserProfileDetailsResponse {

    private UserSnippetDetails userSnippetDetails;

    private int noOfEvents;

    private List<EventResponseDto> pastEvents;

    private List<EventResponseDto> upcomingEvents;

    @Data
    @Builder
    public static class UserSnippetDetails{
        private String userCoverImage;
        private String userDp;
        private String username;
        private String userIntro;
    }
}
