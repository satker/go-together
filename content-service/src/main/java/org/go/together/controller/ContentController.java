package org.go.together.controller;

import org.go.together.client.ContentClient;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.controllers.FindController;
import org.go.together.service.GroupPhotoService;
import org.go.together.service.PhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@RestController
public class ContentController extends FindController implements ContentClient {
    private final PhotoService photoService;
    private final GroupPhotoService groupPhotoService;

    public ContentController(PhotoService photoService, GroupPhotoService groupPhotoService) {
        super(Arrays.asList(photoService, groupPhotoService));
        this.photoService = photoService;
        this.groupPhotoService = groupPhotoService;
    }

    @Override
    public IdDto saveGroupPhotos(GroupPhotoDto groupPhotoDto) {
        return groupPhotoService.savePhotosForEvent(groupPhotoDto);
    }

    @Override
    public GroupPhotoDto getGroupPhotosById(UUID groupPhotoId) {
        return groupPhotoService.getGroupPhotosById(groupPhotoId);
    }

    @Override
    public String validate(PhotoDto photo) {
        return photoService.validate(photo);
    }

    @Override
    public void delete(UUID eventPhotoId) {
        groupPhotoService.delete(eventPhotoId);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }
}
