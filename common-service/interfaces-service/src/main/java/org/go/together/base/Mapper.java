package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;

public interface Mapper<D extends Dto, E extends IdentifiedEntity> {
    D entityToDto(E entity);

    E dtoToEntity(D dto);

    Collection<D> entitiesToDtos(Collection<E> entites);

    Collection<E> dtosToEntities(Collection<D> dtos);
}
