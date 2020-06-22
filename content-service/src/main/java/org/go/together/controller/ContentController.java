package org.go.together.controller;

import org.go.together.client.ContentClient;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.controllers.FindController;
import org.go.together.service.EventPhotoService;
import org.go.together.service.PhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ContentController extends FindController implements ContentClient {
    private final PhotoService photoService;
    private final EventPhotoService eventPhotoService;

    public ContentController(PhotoService photoService, EventPhotoService eventPhotoService) {
        super(Arrays.asList(photoService, eventPhotoService));
        this.photoService = photoService;
        this.eventPhotoService = eventPhotoService;
    }

    @Override
    public Collection<IdDto> savePhotos(Set<PhotoDto> photos) {
        return photoService.savePhotos(photos);
    }

    @Override
    public Set<PhotoDto> getPhotosByIds(Collection<UUID> photoIds) {
        return photoIds.stream()
                .map(photoService::read)
                .collect(Collectors.toSet());
    }

    @Override
    public void deletePhotoById(Collection<UUID> photoIds) {
        photoIds.forEach(photoService::delete);
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

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }
}
