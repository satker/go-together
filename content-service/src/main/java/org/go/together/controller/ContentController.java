package org.go.together.controller;

import org.go.together.client.ContentClient;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.service.EventPhotoService;
import org.go.together.service.PhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
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
    public Set<EventPhotoDto> savePhotosForEvent(Set<EventPhotoDto> eventPhotoDtos) {
        return eventPhotoService.savePhotosForEvent(eventPhotoDtos);
    }

    @Override
    public EventPhotoDto getPhotosByEventId(UUID eventId) {
        return eventPhotoService.getPhotosByEventId(eventId);
    }

    @Override
    public String validate(PhotoDto photo) {
        return photoService.validate(photo);
    }

    @Override
    public void delete(UUID eventId) {
        eventPhotoService.delete(eventId);
    }
}
