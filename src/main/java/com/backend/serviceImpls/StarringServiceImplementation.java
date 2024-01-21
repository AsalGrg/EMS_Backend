package com.backend.serviceImpls;

import com.backend.dtos.AddStarringDto;
import com.backend.models.Event;
import com.backend.models.EventStarring;
import com.backend.repositories.StarringRepository;
import com.backend.services.CloudinaryUploadService;
import com.backend.services.StarringService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StarringServiceImplementation implements StarringService {

    private StarringRepository starringRepository;

    private CloudinaryUploadService cloudinaryUploadService;

    public StarringServiceImplementation(
            StarringRepository starringRepository,
            CloudinaryUploadService cloudinaryUploadService
    ){
        this.starringRepository= starringRepository;
        this.cloudinaryUploadService= cloudinaryUploadService;
    }

    @Override
    public void saveStarring(List<MultipartFile> starringsPhotos, List<String> starringNames, Event event) {
        EventStarring eventStarring= new EventStarring();

//        int i= 1;
//        for (AddStarringDto addStarring:
//             starrings) {
//
//            if(i==1){
//                eventStarring.setStarring1Name(addStarring.getStarringName());
//                eventStarring.setStarring1Photo(uploadPhoto(addStarring.getStarringPhoto()));
//            }
//
//            if(i==2){
//                eventStarring.setStarring2Name(addStarring.getStarringName());
//                eventStarring.setStarring2Photo(uploadPhoto(addStarring.getStarringPhoto()));
//            }
//
//            if(i==3){
//                eventStarring.setStarring3Name(addStarring.getStarringName());
//                eventStarring.setStarring3Photo(uploadPhoto(addStarring.getStarringPhoto()));
//            }
//
//            if(i==4){
//                eventStarring.setStarring4Name(addStarring.getStarringName());
//                eventStarring.setStarring4Photo(uploadPhoto(addStarring.getStarringPhoto()));
//            }
//
//            if(i==5){
//                eventStarring.setStarring5Name(addStarring.getStarringName());
//                eventStarring.setStarring5Photo(uploadPhoto(addStarring.getStarringPhoto()));
//            }
//            i++;
//        }


        for (int index=0; index<=starringsPhotos.size(); index++){

            if(index==0){
                eventStarring.setStarring1Name(starringNames.get(index));
                eventStarring.setStarring1Photo(uploadPhoto(starringsPhotos.get(index)));
            }

            if(index==1){
                eventStarring.setStarring2Name(starringNames.get(index));
                eventStarring.setStarring2Photo(uploadPhoto(starringsPhotos.get(index)));
            }

            if(index==2){
                eventStarring.setStarring3Name(starringNames.get(index));
                eventStarring.setStarring3Photo(uploadPhoto(starringsPhotos.get(index)));
            }

            if(index==3){
                eventStarring.setStarring4Name(starringNames.get(index));
                eventStarring.setStarring4Photo(uploadPhoto(starringsPhotos.get(index)));
            }

            if(index==4){
                eventStarring.setStarring5Name(starringNames.get(index));
                eventStarring.setStarring5Photo(uploadPhoto(starringsPhotos.get(index)));
            }
        }

        eventStarring.setEvent(event);
        starringRepository.saveStarring(eventStarring);
    }

    private String uploadPhoto(MultipartFile photo){
        return cloudinaryUploadService.uploadImage(photo, "Event Starring");
    }
}
