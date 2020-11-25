package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.ContentClient;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.service.interfaces.GroupPhotoService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ContentController extends FindController implements ContentClient {
    private final GroupPhotoService groupPhotoService;

    @Override
    public GroupPhotoDto readGroupPhotos(UUID groupPhotoId) {
        return groupPhotoService.read(null, groupPhotoId);
    }
}
