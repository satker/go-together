package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.PhotoDto;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupPhotoMapper extends CommonMapper<GroupPhotoDto, GroupPhoto> {
    private final Mapper<PhotoDto, Photo> photoMapper;

    @Override
    public GroupPhotoDto toDto(GroupPhoto entity) {
        GroupPhotoDto groupPhotoDto = new GroupPhotoDto();
        groupPhotoDto.setId(entity.getId());
        groupPhotoDto.setGroupId(entity.getGroupId());
        groupPhotoDto.setCategory(entity.getCategory());
        groupPhotoDto.setPhotos(photoMapper.entitiesToDtos(entity.getPhotos()));
        return groupPhotoDto;
    }

    @Override
    public GroupPhoto toEntity(GroupPhotoDto dto) {
        GroupPhoto groupPhoto = new GroupPhoto();
        groupPhoto.setId(dto.getId());
        groupPhoto.setGroupId(dto.getGroupId());
        groupPhoto.setCategory(dto.getCategory());
        return groupPhoto;
    }
}
