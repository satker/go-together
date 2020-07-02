package org.go.together.logic;

import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.IdentifiedEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Mapper<D extends ComparableDto, E extends IdentifiedEntity> {
    D entityToDto(E entity);

    E dtoToEntity(D dto);

    default Collection<D> entitiesToDtos(Collection<E> entites) {
        return entites.stream()
                .map(this::entityToDto)
                .collect(Collectors.toSet());
    }

    default Collection<E> dtosToEntities(Collection<D> dtos) {
        return dtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toSet());
    }
}
