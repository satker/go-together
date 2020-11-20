package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.GroupPhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
    @GetMapping("/groupPhotos/{groupPhotosId}")
    GroupPhotoDto readGroupPhotos(@PathVariable("groupPhotosId") UUID groupPhotoId);
}
