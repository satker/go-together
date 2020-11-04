package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.EventLikeDto;

import java.util.Set;
import java.util.UUID;

public interface EventLikeService extends CrudService<EventLikeDto>, FindService<EventLikeDto> {
    void deleteByUserId(UUID userId);

    Set<UUID> findLikedEventIdsByUserId(UUID userId);

    void deleteByEventId(UUID eventId);
}
