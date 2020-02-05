package org.go.together.client;

import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "content-service", url = "http://localhost:8099")
public interface ContentClient {
    @PostMapping("/users/photos")
    IdDto save(@RequestBody PhotoDto userPhoto);

    @PostMapping("/photos")
    Set<EventPhotoDto> savePhotosForEvent(@RequestBody Set<EventPhotoDto> eventPhotoDtos);

    @GetMapping("events/{eventId}/photos")
    EventPhotoDto getPhotosByEventId(@PathVariable("eventId") UUID eventId);

    @PostMapping("/validate")
    String validate(@RequestBody PhotoDto photo);

    @DeleteMapping("/events/{eventId}/photos")
    void delete(@PathVariable("eventId") UUID eventId);
}
