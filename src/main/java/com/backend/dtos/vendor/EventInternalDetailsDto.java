package com.backend.dtos.vendor;

import com.backend.dtos.OrderDetailsDto;
import com.backend.dtos.addEvent.EventResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class EventInternalDetailsDto {
    private EventResponseDto eventBasicDetails;
    private List<PromoCodeDetailsDto> promoCodeDetailsDtos;
    private List<OrderDetailsDto> eventOrders;
}
