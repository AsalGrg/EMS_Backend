package com.backend.dtos.draftDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EachStarringDetail {

    private int id;
    private String starringPhoto;
    private String starringName;
    private String starringRole;
}
