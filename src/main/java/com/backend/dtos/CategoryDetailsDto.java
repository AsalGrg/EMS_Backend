package com.backend.dtos;

import com.backend.dtos.addEvent.EventResponseDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder

public class CategoryDetailsDto {

    private List<EventResponseDto> categoryEvents;
    private List<VendorResponseDto> categoryVendors;
}
