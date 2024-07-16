package com.backend.dtos;

import com.backend.dtos.addEvent.EventResponseDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CollectionDescription {

    private int collectionId;
    private String collectionName;
    private String collectionImage;
    private String collectionDescription;
    private List<EventResponseDto> collectionEvents;
}
