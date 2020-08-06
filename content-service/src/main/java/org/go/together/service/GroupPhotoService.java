package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.go.together.notification.dto.GroupPhotoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupPhotoService extends CrudServiceImpl<GroupPhotoDto, GroupPhoto> {
    private PhotoService photoService;

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Override
    protected GroupPhoto enrichEntity(GroupPhoto entity, GroupPhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE || crudOperation == CrudOperation.UPDATE) {
            GroupPhoto groupPhoto = Optional.ofNullable(entity.getId())
                    .map(repository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .orElse(null);

            Set<Photo> oldPhotos = Optional.ofNullable(groupPhoto)
                    .map(GroupPhoto::getPhotos)
                    .orElse(Collections.emptySet());

            Set<Photo> newPhotos = photoService.savePhotos(dto.getPhotos(), oldPhotos);

            if (groupPhoto == null) {
                entity.setPhotos(newPhotos);

            } else {
                groupPhoto.setCategory(entity.getCategory());
                groupPhoto.setGroupId(entity.getGroupId());
                groupPhoto.setPhotos(newPhotos);
                return groupPhoto;
            }
        } else if (crudOperation == CrudOperation.DELETE) {
            entity.getPhotos().stream()
                    .map(Photo::getId)
                    .forEach(photoService::delete);
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "groupPhotos";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
