package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.form.FilterDto;
import org.go.together.dto.form.FormDto;
import org.go.together.dto.form.PageDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;

public interface FindRepository<E extends IdentifiedEntity> {
    Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                Map<FieldDto, FilterDto> filters,
                                                String serviceName,
                                                CustomRepository<E> repository,
                                                Map<String, FieldMapper> fieldMappers);
}
