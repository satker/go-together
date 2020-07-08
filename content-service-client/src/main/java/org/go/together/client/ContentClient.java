package org.go.together.client;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.interfaces.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
    @PostMapping("/events/photos")
    IdDto saveGroupPhotos(@RequestBody GroupPhotoDto groupPhotoDto);

    @GetMapping("events/photos/{groupPhotoId}")
    GroupPhotoDto getGroupPhotosById(@PathVariable("groupPhotoId") UUID groupPhotoId);

    @PostMapping("/validate")
    String validate(@RequestBody PhotoDto photo);

    @DeleteMapping("/events/photos/{eventPhotoId}")
    void delete(@PathVariable("eventPhotoId") UUID eventPhotoId);
}
