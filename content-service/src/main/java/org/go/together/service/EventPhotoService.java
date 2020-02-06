package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.PhotoCategory;
import org.go.together.dto.PhotoDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventPhotoMapper;
import org.go.together.model.EventPhoto;
import org.go.together.model.Photo;
import org.go.together.repository.EventPhotoRepository;
import org.go.together.validation.EventPhotoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventPhotoService extends CrudService<EventPhotoDto, EventPhoto> {
    private final EventPhotoRepository eventPhotoRepository;
    private final EventPhotoMapper eventPhotoMapper;
    private PhotoService photoService;

    public EventPhotoService(EventPhotoRepository eventPhotoRepository,
                             EventPhotoMapper eventPhotoMapper,
                             EventPhotoValidator eventPhotoValidator) {
        super(eventPhotoRepository, eventPhotoMapper, eventPhotoValidator);
        this.eventPhotoRepository = eventPhotoRepository;
        this.eventPhotoMapper = eventPhotoMapper;
    }

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    public Set<EventPhotoDto> savePhotosForEvent(Set<EventPhotoDto> eventPhotoDtos) {
        return eventPhotoDtos.stream()
                .map(this::createOrUpdatePhotosByRoomId)
                .map(eventPhotoMapper::entityToDto)
                .collect(Collectors.toSet());

    }

    public EventPhoto createOrUpdatePhotosByRoomId(EventPhotoDto eventPhotoDto) {
        Set<UUID> updatedPhotos = eventPhotoDto.getPhotos().stream()
                .map(PhotoDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        EventPhoto eventPhoto = getEntityEventPhotos(eventPhotoDto.getId());

        Set<UUID> photoIdsFromRepository = eventPhoto.getPhotos().stream()
                .map(Photo::getId)
                .collect(Collectors.toSet());

        Set<Photo> photosForDeleting = eventPhoto.getPhotos().stream()
                .filter(p -> !updatedPhotos.contains(p.getId()))
                .collect(Collectors.toSet());

        Set<Photo> newEventPhotoDtos = eventPhotoDto.getPhotos().stream()
                .filter(photoDto -> photoDto.getId() == null || !photoIdsFromRepository.contains(photoDto.getId()))
                .peek(photoDto -> photoDto.setPhotoCategory(PhotoCategory.EVENT))
                .map(photoService::createEntity)
                .collect(Collectors.toSet());

        if (!photosForDeleting.isEmpty()) {
            deleteContentByRoomId(photosForDeleting);
        }

        Set<Photo> newPhotos = eventPhoto.getPhotos().stream()
                .filter(photo -> photosForDeleting.stream().map(Photo::getId).noneMatch(deletedPhoto -> deletedPhoto.equals(photo.getId())))
                .collect(Collectors.toSet());
        newPhotos.addAll(newEventPhotoDtos);

        eventPhoto.setPhotos(newPhotos);
        return eventPhotoRepository.save(eventPhoto);
    }

    public EventPhotoDto getPhotosByEventId(UUID eventPhotoId) {
        return eventPhotoMapper.entityToDto(getEntityEventPhotos(eventPhotoId));
    }

    private EventPhoto getEntityEventPhotos(UUID eventId) {
        return eventPhotoRepository.findByEventId(eventId)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find photos by event id " + eventId));
    }

    private void deleteContentByRoomId(Set<Photo> photos) {
        photos.stream()
                .filter(photo -> StringUtils.isNotBlank(photo.getPathName()))
                .map(photo -> new File(photo.getPathName()))
                .forEach(photo -> {
                    boolean delete = photo.delete();
                    if (!delete) {
                        throw new RuntimeException("Cannot delete photo");
                    }
                });
    }

    @Override
    public void delete(UUID uuid) {
        Set<Photo> photos = getEntityEventPhotos(uuid).getPhotos();
        deleteContentByRoomId(photos);
        super.delete(uuid);
    }
}
