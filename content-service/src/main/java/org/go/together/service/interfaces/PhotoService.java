package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.PhotoDto;
import org.go.together.model.Photo;

import java.util.Optional;
import java.util.UUID;

public interface PhotoService extends CrudService<PhotoDto>, FindService<PhotoDto> {
    Optional<Photo> readPhoto(UUID photoId);
}
