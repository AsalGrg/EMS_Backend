package com.backend.dtos.addEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventStarringDetails {

    private MultipartFile starring1Photo;
    private String starring1Name;
    private String starring1Role;

    private MultipartFile starring2Photo;
    private String starring2Name;
    private String starring2Role;

    private MultipartFile starring3Photo;
    private String starring3Name;
    private String starring3Role;

    private MultipartFile starring4Photo;
    private String starring4Name;
    private String starring4Role;

    private MultipartFile starring5Photo;
    private String starring5Name;
    private String starring5Role;

}
