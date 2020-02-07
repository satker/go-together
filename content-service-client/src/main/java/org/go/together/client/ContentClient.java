package org.go.together.client;

import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "content-service", url = "http://localhost:8099")
public interface ContentClient {
    @PostMapping("/users/photos")
    IdDto save(@RequestBody PhotoDto userPhoto);

    @PostMapping("/photos")
    IdDto savePhotosForEvent(@RequestBody EventPhotoDto eventPhotoDto);

    @GetMapping("events/photos/{eventPhotoId}")
    EventPhotoDto getEventPhotosById(@PathVariable("eventPhotoId") UUID eventPhotoId);

    @PostMapping("/validate")
    String validate(@RequestBody PhotoDto photo);

    @DeleteMapping("/events/photos/{eventPhotoId}")
    void delete(@PathVariable("eventPhotoId") UUID eventPhotoId);
}
