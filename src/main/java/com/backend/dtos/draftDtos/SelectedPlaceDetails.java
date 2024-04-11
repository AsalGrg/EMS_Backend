package com.backend.dtos.draftDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SelectedPlaceDetails {
    private double lat;
    private double lon;
    private String display_name;
    private String country;
}
