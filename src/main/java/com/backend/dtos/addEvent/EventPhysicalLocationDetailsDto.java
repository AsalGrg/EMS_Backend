package com.backend.dtos.addEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventPhysicalLocationDetailsDto {

    private String displayName;
    private String country;
    private double lon;
    private double lat;
}
