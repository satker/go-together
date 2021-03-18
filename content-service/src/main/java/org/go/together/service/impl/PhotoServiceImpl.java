package org.go.together.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CommonCrudService;
import org.go.together.dto.PhotoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.model.Photo;
import org.go.together.service.interfaces.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
                    throw new ApplicationException("Cannot create image");
                }
            } else {
                entity.setPhotoUrl(dto.getPhotoUrl());
            }
        } else if (crudOperation == CrudOperation.DELETE) {
            if (StringUtils.isNotBlank(entity.getPathName()) && StringUtils.isNotBlank(entity.getContentType())) {
                String filePath = entity.getPathName();
                boolean delete = new File(filePath).delete();
                if (!delete) {
                    throw new ApplicationException("Cannot delete photo");
                }
            }
        }
        return entity;
    }

    @Override
    public Optional<Photo> readPhoto(UUID photoId) {
        if (Objects.isNull(photoId)) {
            return Optional.empty();
        }
        return repository.findById(photoId);
    }

    @Override
    public String getServiceName() {
        return "photos";
    }
}
