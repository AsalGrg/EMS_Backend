package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventAccessRequestsView {

    private String username;
    private String email;

    //asking event access for which event
    private String event_name;

    //other user details
}
