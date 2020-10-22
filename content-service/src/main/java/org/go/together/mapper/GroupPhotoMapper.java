package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.PhotoDto;
import org.go.together.model.GroupPhoto;
import org.go.together.model.Photo;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupPhotoMapper implements Mapper<GroupPhotoDto, GroupPhoto> {
    private final Mapper<PhotoDto, Photo> photoMapper;

    @Override
    public GroupPhotoDto entityToDto(GroupPhoto entity) {
        GroupPhotoDto groupPhotoDto = new GroupPhotoDto();
        groupPhotoDto.setId(entity.getId());
        groupPhotoDto.setGroupId(entity.getGroupId());
        Set<PhotoDto> photos = entity.getPhotos().stream()
                .map(photoMapper::entityToDto)
                .collect(Collectors.toSet());
        groupPhotoDto.setCategory(entity.getCategory());
        groupPhotoDto.setPhotos(photos);
        return groupPhotoDto;
    }

    @Override
    public GroupPhoto dtoToEntity(GroupPhotoDto dto) {
        GroupPhoto groupPhoto = new GroupPhoto();
        groupPhoto.setId(dto.getId());
        groupPhoto.setGroupId(dto.getGroupId());
        groupPhoto.setCategory(dto.getCategory());
        return groupPhoto;
    }
}
