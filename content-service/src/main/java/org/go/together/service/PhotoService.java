package org.go.together.service;

import org.go.together.dto.PhotoDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.PhotoMapper;
import org.go.together.model.Photo;
import org.go.together.repository.PhotoRepository;
import org.go.together.validation.PhotoValidator;
import org.springframework.stereotype.Service;

@Service
public class PhotoService extends CrudService<PhotoDto, Photo> {
    private final PhotoMapper photoMapper;
    private final PhotoValidator photoValidator;
    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository, PhotoMapper photoMapper,
                        PhotoValidator photoValidator) {
        super(photoRepository, photoMapper, photoValidator);
        this.photoMapper = photoMapper;
        this.photoValidator = photoValidator;
        this.photoRepository = photoRepository;
    }

    public Photo createEntity(PhotoDto photo) {
        Photo photoEntity = photoMapper.dtoToEntity(photo);
        return photoRepository.save(photoEntity);
    }

    public String validate(PhotoDto photo) {
        return photoValidator.validate(photo);
    }
}
