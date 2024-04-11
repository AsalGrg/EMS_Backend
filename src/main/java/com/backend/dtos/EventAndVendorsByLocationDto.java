package com.backend.dtos;

import com.backend.dtos.addEvent.EventResponseDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder

public class EventAndVendorsByLocationDto {

    List<EventResponseDto> events;
    List<VendorResponseDto> vendors;
}
