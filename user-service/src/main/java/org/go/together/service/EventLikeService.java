package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.EventLikeDto;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.EventLike;
import org.go.together.repository.EventLikeRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventLikeService extends CrudServiceImpl<EventLikeDto, EventLike> {
    @Override
    public String getServiceName() {
        return "eventLike";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }

    @Override
    protected EventLike enrichEntity(EventLike entity, EventLikeDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            EventLike eventLike = repository.findByIdOrThrow(dto.getId());
            eventLike.setUsers(entity.getUsers());
            return eventLike;
        }
        return entity;
    }

    public Set<EventLikeDto> findUsersLikedEventIds(Set<UUID> eventIds) {
        return eventIds.stream()
                .map(((EventLikeRepository) repository)::findByEventId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(mapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public void deleteByEventId(UUID eventId) {
        Optional<EventLike> eventLikeOptional = ((EventLikeRepository) repository).findByEventId(eventId);
        if (eventLikeOptional.isPresent()) {
            EventLike eventLike = eventLikeOptional.get();
            super.delete(eventLike.getId());
        }
    }

    public Set<UUID> findLikedEventIdsByUserId(UUID userId) {
        return ((EventLikeRepository) repository).findByUserId(userId).stream()
                .map(EventLike::getEventId)
                .collect(Collectors.toSet());
    }

    public void deleteByUserId(UUID userId) {
        ((EventLikeRepository) repository).findByUserId(userId).stream()
                .peek(eventLike -> {
                    eventLike.getUsers().removeIf(user -> user.getId().equals(userId));
                    eventLike.setUsers(eventLike.getUsers());
                }).map(mapper::entityToDto)
                .forEach(super::update);
    }
}
