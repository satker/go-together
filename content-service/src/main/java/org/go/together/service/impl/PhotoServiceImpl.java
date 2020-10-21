package org.go.together.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Photo;
import org.go.together.service.interfaces.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl extends CommonCrudService<PhotoDto, Photo> implements PhotoService {
    private final String photoPath;

    public PhotoServiceImpl(@Value("${photo.store.path}") String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    protected Photo enrichEntity(Photo entity, PhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE || crudOperation == CrudOperation.UPDATE) {
            if (StringUtils.isBlank(dto.getPhotoUrl())) {
                String type = dto.getContent().getType();
                entity.setContentType(type);
                String typeFile = type
                        .replaceAll(";base64,", "")
                        .replaceAll("data:image/", "");
                String filePath = photoPath + entity.getId() + "." + typeFile;
                try {
                    FileUtils.writeByteArrayToFile(new File(filePath),
                            dto.getContent().getPhotoContent());
                    entity.setPathName(filePath);
                } catch (IOException e) {
                    throw new RuntimeException("Cannot create image");
                }
            } else {
                entity.setPhotoUrl(dto.getPhotoUrl());
            }
        } else if (crudOperation == CrudOperation.DELETE) {
            if (StringUtils.isNotBlank(entity.getPathName()) && StringUtils.isNotBlank(entity.getContentType())) {
                String filePath = entity.getPathName();
                boolean delete = new File(filePath).delete();
                if (!delete) {
                    throw new RuntimeException("Cannot delete photo");
                }
            }
        }
        return entity;
    }

    public Set<Photo> savePhotos(Set<PhotoDto> newPhotosDto, Set<Photo> oldPhotos) {
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
                .map(super::create)
                .map(IdDto::getId)
                .map(repository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (!photosForDeleting.isEmpty()) {
            photosForDeleting.stream()
                    .map(Photo::getId)
                    .forEach(super::delete);
        }

        Set<Photo> newPhotos = oldPhotos.stream()
                .filter(photo -> photosForDeleting.stream()
                        .map(Photo::getId)
                        .noneMatch(deletedPhoto -> deletedPhoto.equals(photo.getId())))
                .collect(Collectors.toSet());
        newPhotos.addAll(newEventPhotoDtos);
        return newPhotos;
    }

    @Override
    public String getServiceName() {
        return "photo";
    }
}
