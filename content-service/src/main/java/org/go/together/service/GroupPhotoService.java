package org.go.together.service;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
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

import java.util.*;

@Service
public class GroupPhotoService extends CrudService<GroupPhotoDto, GroupPhoto> {
    private final GroupPhotoRepository groupPhotoRepository;
    private final GroupPhotoMapper groupPhotoMapper;
    private PhotoService photoService;

    public GroupPhotoService(GroupPhotoRepository groupPhotoRepository,
                             GroupPhotoMapper groupPhotoMapper,
                             GroupPhotoValidator groupPhotoValidator) {
        super(groupPhotoRepository, groupPhotoMapper, groupPhotoValidator);
        this.groupPhotoRepository = groupPhotoRepository;
        this.groupPhotoMapper = groupPhotoMapper;
    }

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    public IdDto savePhotosForEvent(GroupPhotoDto groupPhotoDto) {
        GroupPhoto groupPhoto = savePhotosByEventPhotoDto(groupPhotoDto);
        return new IdDto(groupPhoto.getId());

    }

    public GroupPhoto savePhotosByEventPhotoDto(GroupPhotoDto groupPhotoDto) {
        GroupPhoto groupPhoto = Optional.ofNullable(groupPhotoDto.getGroupId())
                .map(groupPhotoRepository::findByEventId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);

        Set<Photo> oldPhotos = Optional.ofNullable(groupPhoto)
                .map(GroupPhoto::getPhotos)
                .orElse(Collections.emptySet());

        Set<Photo> newPhotos = photoService.savePhotos(groupPhotoDto.getPhotos(), oldPhotos);

        if (groupPhoto == null) {
            groupPhoto = groupPhotoMapper.dtoToEntity(groupPhotoDto);
        } else {
            groupPhoto.setPhotos(newPhotos);
        }
        return groupPhotoRepository.save(groupPhoto);
    }


    @Override
    protected void enrichEntity(GroupPhoto entity, GroupPhotoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.DELETE) {
            photoService.deletePhotos(entity.getPhotos());
        }
    }

    public GroupPhotoDto getGroupPhotosById(UUID groupPhotoId) {
        return groupPhotoRepository.findById(groupPhotoId)
                .map(groupPhotoMapper::entityToDto)
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
