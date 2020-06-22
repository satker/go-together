package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoCategory;
import org.go.together.dto.PhotoDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.PhotoMapper;
import org.go.together.model.Photo;
import org.go.together.repository.PhotoRepository;
import org.go.together.validation.PhotoValidator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService extends CrudService<PhotoDto, Photo> {
    private final PhotoMapper photoMapper;
    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository, PhotoMapper photoMapper,
                        PhotoValidator photoValidator) {
        super(photoRepository, photoMapper, photoValidator);
        this.photoMapper = photoMapper;
        this.photoRepository = photoRepository;
    }

    public Photo createEntity(PhotoDto photo) {
        Photo photoEntity = photoMapper.dtoToEntity(photo);
        return photoRepository.save(photoEntity);
    }

    public Collection<IdDto> savePhotos(Set<PhotoDto> photos) {
        return saveOfUpdatePhotos(photos, Collections.emptySet()).stream()
                .map(Photo::getId)
                .map(IdDto::new)
                .collect(Collectors.toSet());
    }

    public Set<Photo> saveOfUpdatePhotos(Set<PhotoDto> newPhotosDto, Set<Photo> oldPhotos) {
        Set<UUID> updatedPhotos = newPhotosDto.stream()
                .map(PhotoDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<UUID> photoIdsFromRepository = oldPhotos.stream()
                .map(Photo::getId)
                .collect(Collectors.toSet());

        Set<Photo> photosForDeleting = oldPhotos.stream()
                .filter(p -> !updatedPhotos.contains(p.getId()))
                .collect(Collectors.toSet());

        Set<Photo> newEventPhotoDtos = newPhotosDto.stream()
                .filter(photoDto -> photoDto.getId() == null || !photoIdsFromRepository.contains(photoDto.getId()))
                .peek(photoDto -> photoDto.setPhotoCategory(PhotoCategory.EVENT))
                .map(this::createEntity)
                .collect(Collectors.toSet());

        if (!photosForDeleting.isEmpty()) {
            deleteContentByRoomId(photosForDeleting);
        }

        Set<Photo> newPhotos = oldPhotos.stream()
                .filter(photo -> photosForDeleting.stream()
                        .map(Photo::getId)
                        .noneMatch(deletedPhoto -> deletedPhoto.equals(photo.getId())))
                .collect(Collectors.toSet());
        newPhotos.addAll(newEventPhotoDtos);
        return newPhotos;
    }

    public void deleteContentByRoomId(Set<Photo> photos) {
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
    public String getServiceName() {
        return "photo";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
