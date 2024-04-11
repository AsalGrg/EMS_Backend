package com.backend.dtos.aboutEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class EachStarring {

    private int id;
    private String starringName;
    private String starringPhoto;
}
