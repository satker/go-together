package org.go.together.find.logic.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.form.FormDto;
import org.go.together.dto.form.PageDto;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;

public interface BaseFindService<E extends IdentifiedEntity> {
    Pair<PageDto, Collection<Object>> find(CustomRepository<E> repository,
                                           FormDto formDto,
                                           String serviceName,
                                           Map<String, FieldMapper> mappingFields);
}
