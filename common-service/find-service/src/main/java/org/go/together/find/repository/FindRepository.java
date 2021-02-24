package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CustomRepository;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.dto.PageDto;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.model.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;

public interface FindRepository<E extends IdentifiedEntity> {
    Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                Collection<Collection<FilterNodeBuilder>> filters,
                                                String serviceName,
                                                CustomRepository<E> repository,
                                                Map<String, FieldMapper> fieldMappers);
}
