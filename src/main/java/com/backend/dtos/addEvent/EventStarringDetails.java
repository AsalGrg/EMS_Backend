package com.backend.dtos.addEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventStarringDetails {

    private String starring1Name;
    private String starring1Role;

    private String starring2Name;
    private String starring2Role;

    private String starring3Name;
    private String starring3Role;

    private String starring4Name;
    private String starring4Role;

    private String starring5Name;
    private String starring5Role;

}
