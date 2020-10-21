package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.PhotoDto;
import org.go.together.find.FindService;
import org.go.together.model.Photo;

import java.util.Set;

public interface PhotoService extends CrudService<PhotoDto>, FindService<Photo> {
    Set<Photo> savePhotos(Set<PhotoDto> newPhotosDto, Set<Photo> oldPhotos);
}
