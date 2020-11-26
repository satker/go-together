package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Mapper<D extends Dto, E extends IdentifiedEntity> {
    D entityToDto(UUID requestId, E entity);

    E dtoToEntity(D dto);

    default Collection<D> entitiesToDtos(UUID requestId, Collection<E> entites) {
        return entites.stream()
                .map(entity -> entityToDto(requestId, entity))
                .collect(Collectors.toSet());
    }

    default Collection<E> dtosToEntities(Collection<D> dtos) {
        return dtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toSet());
    }
}
