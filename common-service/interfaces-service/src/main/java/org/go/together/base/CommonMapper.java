package org.go.together.base;

import org.go.together.base.async.AsyncMapper;
import org.go.together.dto.Dto;
import org.go.together.model.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CommonMapper<D extends Dto, E extends IdentifiedEntity> implements Mapper<D, E> {
    protected AsyncMapper asyncMapper;

    @Autowired
    public void setAsyncMapper(AsyncMapper asyncMapper) {
        this.asyncMapper = asyncMapper;
    }


    public D entityToDto(E entity) {
        D dto = toDto(entity);
        asyncMapper.startAndAwait();
        return dto;
    }

    public E dtoToEntity(D dto) {
        return toEntity(dto);
    }

    public Collection<D> entitiesToDtos(Collection<E> entites) {
        Set<D> collect = entites.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
        asyncMapper.startAndAwait();
        return collect;
    }

    public Collection<E> dtosToEntities(Collection<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    protected abstract D toDto(E entity);

    protected abstract E toEntity(D dto);
}
