package org.go.together.find.logic.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.form.FilterDto;
import org.go.together.dto.form.FormDto;
import org.go.together.dto.form.PageDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.logic.interfaces.BaseCorrectorService;
import org.go.together.find.logic.interfaces.BaseFindService;
import org.go.together.find.repository.FindRepository;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BaseFindServiceImpl<E extends IdentifiedEntity> implements BaseFindService<E> {
    private final BaseCorrectorService baseCorrectorService;
    private final FindRepository<E> findRepository;

    public Pair<PageDto, Collection<Object>> find(CustomRepository<E> repository,
                                                  FormDto formDto,
                                                  String serviceName,
                                                  Map<String, FieldMapper> mappingFields) {
        if (Optional.ofNullable(formDto.getFilters()).map(Map::isEmpty).orElse(true)) {
            return findRepository.getResult(formDto, null, serviceName, repository, mappingFields);
        }
        Map<FieldDto, FilterDto> commonFilters = baseCorrectorService.getCorrectedFilters(formDto, mappingFields);
        if (commonFilters.isEmpty()) {
            PageDto notFoundPageDto = null;
            if (formDto.getPage() != null) {
                notFoundPageDto = new PageDto(0, formDto.getPage().getSize(), 0L, formDto.getPage().getSort());
            }
            return Pair.of(notFoundPageDto, Collections.emptyList());
        }

        return findRepository.getResult(formDto, commonFilters, serviceName, repository, mappingFields);
    }
}
