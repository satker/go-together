package org.go.together.find.logic.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.Mapper;
import org.go.together.dto.Dto;
import org.go.together.dto.form.PageDto;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;
import java.util.UUID;

public interface BaseResultMapper<D extends Dto, E extends IdentifiedEntity> {
    Collection<Object> getParsedResult(UUID requestId, Pair<PageDto, Collection<Object>> pageDtoResult, Mapper<D, E> mapper);
}
