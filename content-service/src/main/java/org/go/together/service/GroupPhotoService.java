package org.go.together.service;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.GroupPhotoMapper;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.go.together.repository.GroupPhotoRepository;
import org.go.together.validation.GroupPhotoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupPhotoService extends CrudService<GroupPhotoDto, GroupPhoto> {
    private final GroupPhotoRepository groupPhotoRepository;
    private PhotoService photoService;

    public GroupPhotoService(GroupPhotoRepository groupPhotoRepository,
                             GroupPhotoMapper groupPhotoMapper,
                             GroupPhotoValidator groupPhotoValidator) {
        super(groupPhotoRepository, groupPhotoMapper, groupPhotoValidator);
        this.groupPhotoRepository = groupPhotoRepository;
    }

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Override
    protected GroupPhoto enrichEntity(GroupPhoto entity, GroupPhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE || crudOperation == CrudOperation.UPDATE) {
            GroupPhoto groupPhoto = Optional.ofNullable(entity.getId())
                    .map(groupPhotoRepository::findById)
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
                entity = groupPhoto;
                entity.setPhotos(newPhotos);
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
