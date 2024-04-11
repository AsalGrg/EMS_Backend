package com.backend.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

public class EventAccessDetails {
    private String accessPassword;
}
