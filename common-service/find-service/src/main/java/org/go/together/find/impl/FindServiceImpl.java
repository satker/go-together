package org.go.together.find.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.FindService;
import org.go.together.find.correction.CorrectedService;
import org.go.together.find.dto.FilterDto;
import org.go.together.find.dto.FindSqlOperator;
import org.go.together.find.dto.FormDto;
import org.go.together.find.dto.PageDto;
import org.go.together.find.finders.Finder;
import org.go.together.find.repository.FindRepository;
import org.go.together.find.repository.FindRepositoryImpl;
import org.go.together.find.utils.FindUtils;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class FindServiceImpl<E extends IdentifiedEntity> implements FindService {
    protected CustomRepository<E> repository;
    private Finder remoteFindService;
    private CorrectedService correctedService;

    @Autowired
    public void setRepository(CustomRepository<E> repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRemoteFindService(Finder finder) {
        this.remoteFindService = finder;
    }

    @Autowired
    public void setCorrectedService(CorrectedService correctedService) {
        this.correctedService = correctedService;
    }

    public Pair<PageDto, Collection<Object>> findByFormDto(FormDto formDto) {
        FindRepository findRepository = new FindRepositoryImpl<>(getServiceName(), repository);
        if (Optional.ofNullable(formDto.getFilters()).map(Map::isEmpty).orElse(true)) {
            return findRepository.getResult(formDto.getMainIdField(), null, formDto.getPage());
        }
        Map<String, FilterDto> commonService = getFilters(formDto);
        if (commonService == null) {
            PageDto notFoundPageDto = null;
            if (formDto.getPage() != null) {
                notFoundPageDto = new PageDto(0, formDto.getPage().getSize(), 0L, formDto.getPage().getSort());
            }
            return Pair.of(notFoundPageDto, Collections.emptyList());
        }
        return findRepository.getResult(formDto.getMainIdField(), commonService, formDto.getPage());
    }

    private Map<String, FilterDto> getFilters(FormDto formDto) {
        Pair<Map<String, FilterDto>, Map<String, FilterDto>>
                correctedFilters = correctedService.getRemoteAndCorrectedFilters(formDto.getFilters(), getMappingFields());
        Map<String, Collection<Object>> remoteFilters = new HashMap<>();
        if (!correctedFilters.getLeft().isEmpty()) {
            remoteFilters = remoteFindService.getFilters(correctedFilters.getLeft(), getMappingFields());
        }
        if (remoteFilters == null) {
            return null;
        }
        return getConcatRemoteAndLocalFilters(remoteFilters, correctedFilters.getRight());
    }

    private Map<String, FilterDto> getConcatRemoteAndLocalFilters(Map<String, Collection<Object>> remoteFilters,
                                                                  Map<String, FilterDto> localFilters) {
        remoteFilters.forEach((key, values) -> {
            FilterDto filterDto = new FilterDto(FindSqlOperator.IN,
                    Collections.singleton(Collections.singletonMap(getCorrectFilterValuesKey(key), values)));
            Map<String, FilterDto> filterForCurrentService = Collections.singletonMap(FindUtils.getParsedRemoteField(key)[0], filterDto);
            localFilters.remove(key);
            localFilters.putAll(filterForCurrentService);
        });

        return localFilters;
    }

    private String getCorrectFilterValuesKey(String key) {
        String[] splitedKey = FindUtils.getParsedRemoteField(key);
        if (splitedKey.length > 1) {
            String[] splitByDotString = FindUtils.getParsedFields(splitedKey[0]);
            return splitByDotString[splitByDotString.length - 1];
        }
        return key;
    }
}
