package org.go.together.find.logic.interfaces;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.node.FilterNodeBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface BaseCorrectorService {
    Collection<Collection<FilterNodeBuilder>> getCorrectedFilters(UUID requestId, FormDto formDto, Map<String, FieldMapper> mappingFields);
}
