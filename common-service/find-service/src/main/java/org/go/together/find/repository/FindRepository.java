package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CustomRepository;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FormDto;
import org.go.together.dto.PageDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;

public interface FindRepository<E extends IdentifiedEntity> {
    Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                Map<FieldDto, FilterDto> filters,
                                                String serviceName,
                                                CustomRepository<E> repository,
                                                Map<String, FieldMapper> fieldMappers);
}
