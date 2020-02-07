package org.go.together.controller;

import org.go.together.client.ContentClient;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.service.EventPhotoService;
import org.go.together.service.PhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ContentController implements ContentClient {
    private final PhotoService photoService;
    private final EventPhotoService eventPhotoService;

    public ContentController(PhotoService photoService, EventPhotoService eventPhotoService) {
        this.photoService = photoService;
        this.eventPhotoService = eventPhotoService;
    }

    @Override
    public IdDto save(PhotoDto userPhoto) {
        return photoService.create(userPhoto);
    }

    @Override
    public IdDto savePhotosForEvent(EventPhotoDto eventPhotoDto) {
        return eventPhotoService.savePhotosForEvent(eventPhotoDto);
    }

    @Override
    public EventPhotoDto getEventPhotosById(UUID eventPhotoId) {
        return eventPhotoService.getEventPhotosById(eventPhotoId);
    }

    @Override
    public String validate(PhotoDto photo) {
        return photoService.validate(photo);
    }

    @Override
    public void delete(UUID eventPhotoId) {
        eventPhotoService.delete(eventPhotoId);
    }
}
