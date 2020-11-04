package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.GroupPhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
    @GetMapping("events/photos/{groupPhotoId}")
    GroupPhotoDto readGroupPhotosById(@PathVariable("groupPhotoId") UUID groupPhotoId);

    @DeleteMapping("/events/photos/{eventPhotoId}")
    void delete(@PathVariable("eventPhotoId") UUID eventPhotoId);
}
