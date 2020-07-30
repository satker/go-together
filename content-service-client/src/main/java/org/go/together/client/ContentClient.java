package org.go.together.client;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.interfaces.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
    @PutMapping("/groups")
    IdDto createGroup(@RequestBody GroupPhotoDto groupPhotoDto);

    @PostMapping("/groups")
    IdDto updateGroup(@RequestBody GroupPhotoDto groupPhotoDto);

    @GetMapping("events/photos/{groupPhotoId}")
    GroupPhotoDto readGroupPhotosById(@PathVariable("groupPhotoId") UUID groupPhotoId);

    @PostMapping("/validate")
    String validate(@RequestBody GroupPhotoDto groupPhotoDto);

    @DeleteMapping("/events/photos/{eventPhotoId}")
    void delete(@PathVariable("eventPhotoId") UUID eventPhotoId);
}
