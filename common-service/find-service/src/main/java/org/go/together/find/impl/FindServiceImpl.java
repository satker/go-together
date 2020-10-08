package org.go.together.find.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.find.FindService;
import org.go.together.find.correction.CorrectorService;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.form.PageDto;
import org.go.together.find.dto.utils.FindSqlOperator;
import org.go.together.find.finders.Finder;
import org.go.together.find.repository.FindRepository;
import org.go.together.find.repository.FindRepositoryImpl;
import org.go.together.find.utils.FindUtils;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

public abstract class FindServiceImpl<E extends IdentifiedEntity> implements FindService<E> {
    protected CustomRepository<E> repository;
    private Finder remoteFindService;
    private CorrectorService correctorRemoteFiltersService;
    private CorrectorService correctorLocalFiltersService;
    private FieldPathCorrector fieldPathCorrector;

    @Autowired
    public void setRepository(CustomRepository<E> repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRemoteFindService(Finder finder) {
        this.remoteFindService = finder;
    }

    @Autowired
    public void setRemoteCorrectedService(@Qualifier("remoteFilters") CorrectorService correctorRemoteFiltersService) {
        this.correctorRemoteFiltersService = correctorRemoteFiltersService;
    }

    @Autowired
    public void setLocalCorrectedService(@Qualifier("localFilters") CorrectorService correctorLocalFiltersService) {
        this.correctorLocalFiltersService = correctorLocalFiltersService;
    }

    @Autowired
    public void setFieldPathCorrector(FieldPathCorrector fieldPathCorrector) {
        this.fieldPathCorrector = fieldPathCorrector;
    }

    public Pair<PageDto, Collection<Object>> findByFormDto(FormDto formDto) {
        FindRepository findRepository = new FindRepositoryImpl<>(getServiceName(),
                repository,
                getMappingFields(),
                fieldPathCorrector);

        if (Optional.ofNullable(formDto.getFilters()).map(Map::isEmpty).orElse(true)) {
            return findRepository.getResult(formDto, null);
        }
        Map<FieldDto, FilterDto> commonService = getFilters(formDto);
        if (commonService == null) {
            PageDto notFoundPageDto = null;
            if (formDto.getPage() != null) {
                notFoundPageDto = new PageDto(0, formDto.getPage().getSize(), 0L, formDto.getPage().getSort());
            }
            return Pair.of(notFoundPageDto, Collections.emptyList());
        }

        return findRepository.getResult(formDto, commonService);
    }

    private Map<FieldDto, FilterDto> getFilters(FormDto formDto) {
        Map<String, FieldMapper> mappingFields = getMappingFields();
        Map<FieldDto, FilterDto> remoteFilters = correctorRemoteFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        Map<FieldDto, Collection<Object>> resultRemoteFilters = new HashMap<>();
        if (!remoteFilters.isEmpty()) {
            resultRemoteFilters = remoteFindService.getFilters(remoteFilters, mappingFields);
        }
        if (resultRemoteFilters == null) {
            return null;
        }
        Map<FieldDto, FilterDto> localFilters = correctorLocalFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        return mergeFilters(resultRemoteFilters, localFilters);
    }

    private Map<FieldDto, FilterDto> mergeFilters(Map<FieldDto, Collection<Object>> remoteFilters,
                                                  Map<FieldDto, FilterDto> localFilters) {
        boolean isNotFound = remoteFilters.values().stream().anyMatch(Collection::isEmpty);
        if (isNotFound) {
            return null;
        }
        remoteFilters.forEach((key, values) -> {
            FilterDto filterDto = new FilterDto(FindSqlOperator.IN,
                    Collections.singleton(Collections.singletonMap(getCorrectFilterValuesKey(key), values)));
            localFilters.remove(key);
            localFilters.put(key, filterDto);
        });

        return localFilters;
    }

    private String getCorrectFilterValuesKey(FieldDto fieldDto) {
        return Optional.ofNullable(fieldDto.getLocalField())
                .map(FindUtils::getParsedFields)
                .map(splitByDotString -> splitByDotString[splitByDotString.length - 1])
                .orElseThrow(() -> new IncorrectDtoException("Incorrect search field"));
    }
}
