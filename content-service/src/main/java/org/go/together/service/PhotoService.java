package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.PhotoMapper;
import org.go.together.model.Photo;
import org.go.together.repository.PhotoRepository;
import org.go.together.validation.PhotoValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService extends CrudService<PhotoDto, Photo> {
    private final PhotoMapper photoMapper;
    private final PhotoRepository photoRepository;
    private final String photoPath;

    public PhotoService(PhotoRepository photoRepository,
                        PhotoMapper photoMapper,
                        PhotoValidator photoValidator,
                        @Value("${photo.store.path}") String photoPath) {
        super(photoRepository, photoMapper, photoValidator);
        this.photoMapper = photoMapper;
        this.photoRepository = photoRepository;
        this.photoPath = photoPath;
    }

    public Photo createPhotoFile(PhotoDto photo) {
        Photo photoEntity = photoMapper.dtoToEntity(photo);
        try {
            photoRepository.save(photoEntity);
        } catch (Exception e) {
            String filePath = photoPath + photoEntity.getPathName() + "." + photoEntity.getContentType()
                    .replaceAll(";base64,", "")
                    .replaceAll("data:image/", "");
            new File(filePath).delete();
            throw new RuntimeException("Cannot create image");
        }
        return photoRepository.save(photoEntity);
    }

    public Collection<IdDto> savePhotos(Set<PhotoDto> photos) {
        return saveOrUpdatePhotos(photos, Collections.emptySet()).stream()
                .map(Photo::getId)
                .map(IdDto::new)
                .collect(Collectors.toSet());
    }

    public Set<Photo> saveOrUpdatePhotos(Set<PhotoDto> newPhotosDto, Set<Photo> oldPhotos) {
        Set<UUID> presentedPhotos = newPhotosDto.stream()
                .map(PhotoDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<UUID> presentedPhotoIds = oldPhotos.stream()
                .map(Photo::getId)
                .collect(Collectors.toSet());

        Set<Photo> photosForDeleting = oldPhotos.stream()
                .filter(oldPhoto -> !presentedPhotos.contains(oldPhoto.getId()))
                .collect(Collectors.toSet());

        Set<Photo> newEventPhotoDtos = newPhotosDto.stream()
                .filter(photoDto -> photoDto.getId() == null || !presentedPhotoIds.contains(photoDto.getId()))
                .map(this::createPhotoFile)
                .collect(Collectors.toSet());

        if (!photosForDeleting.isEmpty()) {
            deletePhotos(photosForDeleting);
        }

        Set<Photo> newPhotos = oldPhotos.stream()
                .filter(photo -> photosForDeleting.stream()
                        .map(Photo::getId)
                        .noneMatch(deletedPhoto -> deletedPhoto.equals(photo.getId())))
                .collect(Collectors.toSet());
        newPhotos.addAll(newEventPhotoDtos);
        return newPhotos;
    }

    public void deletePhotos(Set<Photo> photos) {
        photos.stream()
                .filter(photo -> StringUtils.isNotBlank(photo.getPathName()))
                .forEach(photo -> {
                    boolean delete = new File(photo.getPathName()).delete();
                    if (!delete) {
                        throw new RuntimeException("Cannot delete photo");
                    } else {
                        photoRepository.delete(photo);
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
