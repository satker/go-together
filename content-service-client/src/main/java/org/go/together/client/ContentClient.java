package org.go.together.client;

import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.PhotoDto;
import org.go.together.interfaces.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
    @PutMapping("/photos")
    Collection<IdDto> savePhotos(@RequestBody Set<PhotoDto> userPhotos);

    @PostMapping("/photos")
    Set<PhotoDto> getPhotosByIds(@RequestBody Collection<UUID> photoIds);

    @DeleteMapping("/photos")
    void deletePhotoById(@RequestBody Collection<UUID> photoIds);

    @PostMapping("/events/photos")
    IdDto savePhotosForEvent(@RequestBody EventPhotoDto eventPhotoDto);

    @GetMapping("events/photos/{eventPhotoId}")
    EventPhotoDto getEventPhotosById(@PathVariable("eventPhotoId") UUID eventPhotoId);

    @PostMapping("/validate")
    String validate(@RequestBody PhotoDto photo);

    @DeleteMapping("/events/photos/{eventPhotoId}")
    void delete(@PathVariable("eventPhotoId") UUID eventPhotoId);
}
