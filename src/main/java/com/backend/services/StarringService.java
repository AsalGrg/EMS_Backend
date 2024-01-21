package com.backend.services;

import com.backend.dtos.AddStarringDto;
import com.backend.models.Event;
import com.backend.models.EventStarring;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StarringService {

    void saveStarring(List<MultipartFile> starringsPhotos, List<String> starringNames, Event event);
}
