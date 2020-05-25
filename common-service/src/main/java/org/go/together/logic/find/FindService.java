package org.go.together.logic.find;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.find.enums.FindSqlOperator;
import org.go.together.logic.find.finders.Finder;
import org.go.together.logic.find.finders.LocalFinder;
import org.go.together.logic.find.finders.RemoteFinder;
import org.go.together.logic.find.repository.FindRepository;
import org.go.together.logic.find.repository.FindRepositoryImpl;
import org.go.together.logic.repository.CustomRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FindService<E extends IdentifiedEntity> {
    private final FindRepository repository;

    private final Finder<Collection<Object>> remoteFindService;
    private final Finder<FilterDto> localFindService;

    protected FindService(CustomRepository<E> repository) {
        this.repository = new FindRepositoryImpl<>(getServiceName(), repository);
        this.remoteFindService = new RemoteFinder();
        localFindService = new LocalFinder();
    }

    public abstract String getServiceName();

    public abstract Map<String, FieldMapper> getMappingFields();

    public Pair<PageDto, Collection<Object>> findByFormDto(FormDto formDto) {
        if (formDto.getFilters().isEmpty()) {
            return repository.getResult(formDto.getMainIdField(), null, formDto.getPage());
        }
        Map<String, FilterDto> commonService = getFilters(formDto);
        if (commonService == null) {
            PageDto notFoundPageDto = null;
            if (formDto.getPage() != null) {
                notFoundPageDto = new PageDto(0, formDto.getPage().getSize(), 0L, formDto.getPage().getSort());
            }
            return Pair.of(notFoundPageDto, Collections.emptyList());
        }
        return repository.getResult(formDto.getMainIdField(), commonService, formDto.getPage());
    }

    private Map<String, FilterDto> getFilters(FormDto formDto) {
        Map<String, FilterDto> localFilters = localFindService.getFilters(formDto.getFilters(), getMappingFields());
        Map<String, Collection<Object>> remoteFilters = remoteFindService.getFilters(formDto.getFilters(), getMappingFields());
        if (remoteFilters == null) {
            return null;
        }
        return getConcatRemoteAndLocalFilters(remoteFilters, localFilters);
    }

    private Map<String, FilterDto> getConcatRemoteAndLocalFilters(Map<String, Collection<Object>> remoteFilters,
                                                                  Map<String, FilterDto> localFilters) {
        remoteFilters.forEach((key, values) -> {
            Set<SimpleDto> valuesFromOtherService = values.stream()
                    .map(value -> new SimpleDto(String.valueOf(value), String.valueOf(value)))
                    .collect(Collectors.toSet());

            FilterDto filterDto = new FilterDto(FindSqlOperator.IN, valuesFromOtherService);
            Map<String, FilterDto> filterForCurrentService = Collections.singletonMap(key, filterDto);
            localFilters.putAll(filterForCurrentService);
        });

        return localFilters;
    }
}
