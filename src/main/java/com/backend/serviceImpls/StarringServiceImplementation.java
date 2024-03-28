package com.backend.serviceImpls;

import com.backend.dtos.AddStarringDto;
import com.backend.dtos.addEvent.EventStarringDetails;
import com.backend.exceptions.InternalServerError;
import com.backend.models.Event;
import com.backend.models.EventStarring;
import com.backend.repositories.StarringRepository;
import com.backend.services.CloudinaryUploadService;
import com.backend.services.StarringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service
@Slf4j
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
    public void saveStarring(EventStarringDetails eventStarringDetails, Event event) {

        EventStarring eventStarringFromDB = starringRepository.getEventStarringByEventId(event.getId());

        if(eventStarringFromDB==null) {
            EventStarring eventStarring = new EventStarring();

            for (int index=1; index<5; index++) {

                try {
                    Method methodDet = eventStarringDetails.getClass().getMethod("getStarring" + index + "Photo");

                    MultipartFile starringImage = (MultipartFile) methodDet.invoke(eventStarringDetails);

                    if(starringImage==null){
                        continue;
                    }

                    Method methodEventStarring = eventStarring.getClass().getMethod("setStarring" + index + "Photo", String.class);
                    methodEventStarring.invoke(eventStarring, uploadPhoto(starringImage));

                    methodDet = eventStarringDetails.getClass().getMethod("getStarring" + index + "Name");
                    methodEventStarring = eventStarring.getClass().getMethod("setStarring" + index + "Name", String.class);
                    Method methodStarringPhotoName= eventStarring.getClass().getMethod("setStarring"+index+"ImgName", String.class);
                    methodStarringPhotoName.invoke(eventStarring,starringImage.getOriginalFilename());

                    String starringName= (String) methodDet.invoke(eventStarringDetails);
                    methodEventStarring.invoke(eventStarring, starringName);

                    eventStarring.setEvent(event);

                    log.info("dfdfdfdf: "+ eventStarringDetails.getStarring2Name());


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            starringRepository.saveStarring(eventStarring);
        }else{
            updateEventStarring(eventStarringDetails, eventStarringFromDB);
        }
    }

    @Override
    public EventStarring getEventStarringByEventId(int eventId) {
        return starringRepository.getEventStarringByEventId(eventId);
    }

    public void updateEventStarring (EventStarringDetails eventStarringDetails, EventStarring eventStarring) {

        for (int index = 1; index < 5; index++) {

            try {
                Method methodStarringPhoto = eventStarringDetails.getClass().getMethod("getStarring" + index + "Photo");
                Method methodStarringName = eventStarringDetails.getClass().getMethod("getStarring"+index+"Name");
//                Method methodStarringPhotoName= eventStarringDetails.getClass().getMethod("getStarring"+index+"ImgName");

                MultipartFile newStarringImage = (MultipartFile) methodStarringPhoto.invoke(eventStarringDetails);
                String newStarringName = (String) methodStarringName.invoke(eventStarringDetails);


                //savedstarring operations
                methodStarringName = eventStarring.getClass().getMethod("getStarring"+index+"Name");
                String savedStarringName = (String) methodStarringName.invoke(eventStarring);


                Method methodStarringPhotoName= eventStarring.getClass().getMethod("getStarring"+index+"ImgName");
                String savedStarringPhotoName = (String)methodStarringPhotoName.invoke(eventStarring);

                Method methodStarringSetStarringPhoto = eventStarring.getClass().getMethod("setStarring"+index+"Photo", String.class);
                Method methodStarringSetStarringImgName = eventStarring.getClass().getMethod("setStarring"+index+"ImgName", String.class);
                Method methodStarringSetStarringName = eventStarring.getClass().getMethod("setStarring"+index+"Name", String.class);


                if (newStarringImage == null) {
                    methodStarringSetStarringPhoto.invoke(eventStarring, (Object) null);
                    methodStarringSetStarringImgName.invoke(eventStarring,(Object) null);
                    methodStarringSetStarringName.invoke(eventStarring, (Object) null);
                    continue;
                }

                if(!newStarringImage.getOriginalFilename().equals(savedStarringPhotoName)){
                    methodStarringSetStarringPhoto.invoke(eventStarring,uploadPhoto(newStarringImage));
                    methodStarringSetStarringImgName.invoke(eventStarring, newStarringImage.getOriginalFilename());
                }

                if(!newStarringName.equals(savedStarringName)){
                    methodStarringSetStarringName.invoke(eventStarring, newStarringName);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        starringRepository.saveStarring(eventStarring);
    }

    private String uploadPhoto(MultipartFile photo){
        return cloudinaryUploadService.uploadImage(photo, "Event Starring");
    }
}
