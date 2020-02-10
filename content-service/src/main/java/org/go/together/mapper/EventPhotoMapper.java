package org.go.together.mapper;

import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.PhotoDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.EventPhoto;
import org.go.together.model.Photo;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventPhotoMapper implements Mapper<EventPhotoDto, EventPhoto> {
    private final PhotoMapper photoMapper;

    public EventPhotoMapper(PhotoMapper photoMapper) {
        this.photoMapper = photoMapper;
    }


    @Override
    public EventPhotoDto entityToDto(EventPhoto entity) {
        EventPhotoDto eventPhotoDto = new EventPhotoDto();
        eventPhotoDto.setId(entity.getId());
        eventPhotoDto.setEventId(entity.getEventId());
        Set<PhotoDto> photos = entity.getPhotos().stream()
                .map(photoMapper::entityToDto)
                .collect(Collectors.toSet());
        eventPhotoDto.setPhotos(photos);
        return eventPhotoDto;
    }

    @Override
    public EventPhoto dtoToEntity(EventPhotoDto dto) {
        EventPhoto apartmentPhoto = new EventPhoto();
        apartmentPhoto.setId(dto.getId());
        apartmentPhoto.setEventId(dto.getEventId());
        Set<Photo> photos = dto.getPhotos().stream()
                .map(photoMapper::dtoToEntity)
                .collect(Collectors.toSet());
        apartmentPhoto.setPhotos(photos);
        return apartmentPhoto;
    }
}
