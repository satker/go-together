package org.go.together.service;

import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventPhotoMapper;
import org.go.together.model.EventPhoto;
import org.go.together.model.Photo;
import org.go.together.repository.EventPhotoRepository;
import org.go.together.validation.EventPhotoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public IdDto savePhotosForEvent(EventPhotoDto eventPhotoDto) {
        EventPhoto eventPhoto = createOrUpdatePhotosByEventPhotoDto(eventPhotoDto);
        return new IdDto(eventPhoto.getId());

    }

    public EventPhoto createOrUpdatePhotosByEventPhotoDto(EventPhotoDto eventPhotoDto) {
        EventPhoto eventPhoto = Optional.ofNullable(eventPhotoDto.getEventId())
                .map(eventPhotoRepository::findByEventId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);

        Set<Photo> newPhotos = photoService.saveOfUpdatePhotos(eventPhotoDto.getPhotos(),
                Optional.ofNullable(eventPhoto)
                        .map(EventPhoto::getPhotos)
                        .orElse(Collections.emptySet()));

        if (eventPhoto == null) {
            eventPhoto = eventPhotoMapper.dtoToEntity(eventPhotoDto);
        } else {
            eventPhoto.setPhotos(newPhotos);
        }
        return eventPhotoRepository.save(eventPhoto);
    }


    @Override
    protected void updateEntity(EventPhoto entity, EventPhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.DELETE) {
            photoService.deleteContentByRoomId(entity.getPhotos());
        }
    }

    public EventPhotoDto getEventPhotosById(UUID eventPhotoId) {
        return eventPhotoRepository.findById(eventPhotoId)
                .map(eventPhotoMapper::entityToDto)
                .orElse(null);
    }

    @Override
    public String getServiceName() {
        return "eventPhoto";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
