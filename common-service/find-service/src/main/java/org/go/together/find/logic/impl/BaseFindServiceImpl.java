package org.go.together.find.logic.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CustomRepository;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.dto.PageDto;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.logic.interfaces.BaseCorrectorService;
import org.go.together.find.logic.interfaces.BaseFindService;
import org.go.together.find.repository.FindRepository;
import org.go.together.model.IdentifiedEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BaseFindServiceImpl<E extends IdentifiedEntity> implements BaseFindService<E> {
    private final BaseCorrectorService baseCorrectorService;
    private final FindRepository<E> findRepository;

    public Pair<PageDto, Collection<Object>> find(UUID requestId,
                                                  CustomRepository<E> repository,
                                                  FormDto formDto,
                                                  String serviceName,
                                                  Map<String, FieldMapper> mappingFields) {
        if (Optional.ofNullable(formDto.getFilters()).map(Map::isEmpty).orElse(true)) {
            return findRepository.getResult(formDto, null, serviceName, repository, mappingFields);
        }
        Collection<Collection<FilterNodeBuilder>> nodeBuilders = baseCorrectorService.getCorrectedFilters(requestId, formDto, mappingFields);
        if (nodeBuilders.isEmpty()) {
            PageDto notFoundPageDto = null;
            if (formDto.getPage() != null) {
                notFoundPageDto = new PageDto(0, formDto.getPage().getSize(), 0L, formDto.getPage().getSort());
            }
            return Pair.of(notFoundPageDto, Collections.emptyList());
        }

        return findRepository.getResult(formDto, nodeBuilders, serviceName, repository, mappingFields);
    }
}
