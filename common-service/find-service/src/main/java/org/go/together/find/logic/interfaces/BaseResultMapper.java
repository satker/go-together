package org.go.together.find.logic.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.Mapper;
import org.go.together.dto.Dto;
import org.go.together.dto.PageDto;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;

public interface BaseResultMapper<D extends Dto, E extends IdentifiedEntity> {
    Collection<Object> getParsedResult(Pair<PageDto, Collection<Object>> pageDtoResult, Mapper<D, E> mapper);
}
