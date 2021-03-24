package org.go.together.find.logic.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.Mapper;
import org.go.together.dto.Dto;
import org.go.together.dto.PageDto;
import org.go.together.find.logic.interfaces.BaseResultMapper;
import org.go.together.interfaces.Identified;
import org.go.together.model.IdentifiedEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaseResultMapperImpl<D extends Dto, E extends IdentifiedEntity> implements BaseResultMapper<D, E> {
    public Collection<Object> getParsedResult(Pair<PageDto, Collection<Object>> pageDtoResult, Mapper<D, E> mapper) {
        Collection<Object> values = pageDtoResult.getValue();
        if (values != null
                && !values.isEmpty()
                && values.iterator().next() instanceof IdentifiedEntity) {
            List<E> entites = values.stream()
                    .map(Identified::<E>cast)
                    .collect(Collectors.toList());
            values = Arrays.asList(mapper.entitiesToDtos(entites).toArray());
        }
        return values;
    }
}
