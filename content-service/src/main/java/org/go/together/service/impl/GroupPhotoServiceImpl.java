package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.go.together.service.interfaces.GroupPhotoService;
import org.go.together.service.interfaces.PhotoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.go.together.enums.ContentServiceInfo.GROUP_PHOTO_NAME;

@Service
@RequiredArgsConstructor
public class GroupPhotoServiceImpl extends CommonCrudService<GroupPhotoDto, GroupPhoto> implements GroupPhotoService {
    private final PhotoService photoService;

    @Override
    protected GroupPhoto enrichEntity(UUID requestId, GroupPhoto entity, GroupPhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE || crudOperation == CrudOperation.UPDATE) {
            Set<Photo> photos = dto.getPhotos().stream()
                    .map(photoDto -> getPhotos(requestId, photoDto))
                    .collect(Collectors.toSet());
            GroupPhoto groupPhoto = repository.findByIdOrThrow(entity.getId());
            deleteUnneededPhotos(requestId, groupPhoto.getPhotos(), photos);
            entity.setPhotos(photos);
        } else if (crudOperation == CrudOperation.DELETE) {
            entity.getPhotos().stream()
                    .map(Photo::getId)
                    .forEach(photoId -> photoService.delete(requestId, photoId));
        }
        return entity;
    }

    private Photo getPhotos(UUID requestId, PhotoDto photoDto) {
        Optional<Photo> photo = photoService.readPhoto(photoDto.getId());
        if (photo.isEmpty()) {
            IdDto createdPhotoId = photoService.create(requestId, photoDto);
            Optional<Photo> createdPhoto = photoService.readPhoto(createdPhotoId.getId());
            return createdPhoto.orElseThrow(() -> new ApplicationException("Cannot create photo by id: " + createdPhotoId.getId(), requestId));
        } else {
            return photo.get();
        }
    }

    private void deleteUnneededPhotos(UUID requestId, Set<Photo> oldPhotos, Set<Photo> newPhotos) {
        Optional.ofNullable(oldPhotos)
                .orElse(Collections.emptySet()).stream()
                .map(Photo::getId)
                .filter(photoId -> newPhotos.stream().noneMatch(photo -> photo.getId().equals(photoId)))
                .forEach(photoId -> photoService.delete(requestId, photoId));
    }

    @Override
    public String getServiceName() {
        return GROUP_PHOTO_NAME;
    }
}
