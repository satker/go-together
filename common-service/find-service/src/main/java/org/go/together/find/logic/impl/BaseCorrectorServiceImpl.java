package org.go.together.find.logic.impl;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.builder.FilterBuilder;
import org.go.together.find.correction.CorrectorService;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.logic.interfaces.BaseCorrectorService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class BaseCorrectorServiceImpl implements BaseCorrectorService {
    private final CorrectorService correctorService;
    private final FilterBuilder filterBuilder;

    public BaseCorrectorServiceImpl(CorrectorService correctorService,
                                    FilterBuilder filterBuilder) {
        this.correctorService = correctorService;
        this.filterBuilder = filterBuilder;
    }


    @Override
    public Collection<Collection<FilterNodeBuilder>> getCorrectedFilters(UUID requestId, FormDto formDto, Map<String, FieldMapper> mappingFields) {
        return formDto.getFilters().entrySet().stream()
                .map(filterBuilder::getBuilders)
                .map(filterNodeBuilder -> correctorService.getCorrectedFilters(requestId, filterNodeBuilder, mappingFields))
                .collect(Collectors.toSet());
    }
}
