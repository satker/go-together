package org.go.together.logic.services;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.CustomRepository;
import org.go.together.dto.filter.*;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.services.find.finders.Finder;
import org.go.together.logic.services.find.finders.RemoteFinder;
import org.go.together.logic.services.find.repository.FindRepository;
import org.go.together.logic.services.find.repository.FindRepositoryImpl;
import org.go.together.logic.services.find.validation.CorrectFieldService;
import org.go.together.utils.FindUtils;

import java.util.*;

import static org.go.together.utils.FindUtils.getParsedString;

public abstract class FindService<E extends IdentifiedEntity> {
    private final CustomRepository<E> repository;

    private final Finder<Collection<Object>> remoteFindService;
    private final CorrectFieldService correctFieldService = new CorrectFieldService();

    protected FindService(CustomRepository<E> repository) {
        this.repository = repository;
        this.remoteFindService = new RemoteFinder();
    }

    public abstract String getServiceName();

    public abstract Map<String, FieldMapper> getMappingFields();

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
                correctedFilters = correctFieldService.getRemoteAndCorrectedFilters(formDto.getFilters(), getMappingFields());
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
            Map<String, FilterDto> filterForCurrentService = Collections.singletonMap(getParsedString(key)[0], filterDto);
            localFilters.remove(key);
            localFilters.putAll(filterForCurrentService);
        });

        return localFilters;
    }

    private String getCorrectFilterValuesKey(String key) {
        String[] splitedKey = FindUtils.getParsedString(key);
        if (splitedKey.length > 1) {
            String[] splitByDotString = FindUtils.getSplitByDotString(splitedKey[0]);
            return splitByDotString[splitByDotString.length - 1];
        }
        return key;
    }
}
