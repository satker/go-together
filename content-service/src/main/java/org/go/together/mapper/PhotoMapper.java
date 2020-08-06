package org.go.together.mapper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.go.together.dto.ContentDto;
import org.go.together.dto.PhotoDto;
import org.go.together.model.Photo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class PhotoMapper implements Mapper<PhotoDto, Photo> {
    public PhotoDto entityToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        if (StringUtils.isNotBlank(photo.getPathName())) {
            try {
                byte[] bytes = FileUtils.readFileToByteArray(new File(photo.getPathName()));
                photoDto.setContent(new ContentDto(photo.getContentType(), bytes));
            } catch (IOException e) {
                throw new RuntimeException("Cannot read image");
            }
        } else {
            photoDto.setPhotoUrl(photo.getPhotoUrl());
        }
        photoDto.setId(photo.getId());
        return photoDto;
    }

    public Photo dtoToEntity(PhotoDto photoDto) {
        UUID id = photoDto.getId() != null ? photoDto.getId() : UUID.randomUUID();
        Photo photo = new Photo();
        photo.setId(id);
        return photo;
    }
}
