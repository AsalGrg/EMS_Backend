package com.backend.dtos.addEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventStarringDetails {

    private Object starring1Photo;
    private String starring1Name;
    private String starring1Role;

    private Object starring2Photo;
    private String starring2Name;
    private String starring2Role;

    private Object starring3Photo;
    private String starring3Name;
    private String starring3Role;

    private Object starring4Photo;
    private String starring4Name;
    private String starring4Role;

    private Object starring5Photo;
    private String starring5Name;
    private String starring5Role;

}
