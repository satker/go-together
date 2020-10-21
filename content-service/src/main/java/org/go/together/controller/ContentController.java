package org.go.together.controller;

import org.go.together.client.ContentClient;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.find.controller.FindController;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.service.interfaces.GroupPhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ContentController extends FindController implements ContentClient {
    private final GroupPhotoService groupPhotoService;

    public ContentController(GroupPhotoService groupPhotoService) {
        this.groupPhotoService = groupPhotoService;
    }

    @Override
    public IdDto createGroup(GroupPhotoDto groupPhotoDto) {
        return groupPhotoService.create(groupPhotoDto);
    }

    @Override
    public IdDto updateGroup(GroupPhotoDto groupPhotoDto) {
        return groupPhotoService.update(groupPhotoDto);
    }

    @Override
    public GroupPhotoDto readGroupPhotosById(UUID groupPhotoId) {
        return groupPhotoService.read(groupPhotoId);
    }

    @Override
    public String validate(GroupPhotoDto groupPhotoDto) {
        return groupPhotoService.validate(groupPhotoDto);
    }

    @Override
    public void delete(UUID groupId) {
        groupPhotoService.delete(groupId);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }
}
