package org.go.together.logic;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.find.LocalFindService;
import org.go.together.logic.find.RemoteFindService;
import org.go.together.logic.repository.CustomRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class FilterService<E extends IdentifiedEntity> extends LocalFindService<E> {
    private final RemoteFindService remoteFindService = new RemoteFindService(getServiceName());

    protected FilterService(CustomRepository<E> repository) {
        super(repository);
    }

    public Pair<PageDto, Collection<Object>> findByFormDto(FormDto formDto) {

        if (formDto.getFilters().isEmpty()) {
            // if filters null search only by main field and group by this field
            return getValuesInCurrentService(formDto.getMainIdField(), null, formDto.getPage());
        }

        // Filters for current service
        Map<String, FilterDto> currentFilters = new HashMap<>();

        // Get filters result to another service : OK
        Map<String, Collection<Object>> filterKeysFromOtherServices = remoteFindService.getFiltersToRequest(currentFilters,
                formDto.getFilters(), getMappingFields());
        ////////////////

        // get values from this service + other service result
        Map<String, FilterDto> filtersToCurrentService = enrichFilterByFoundedValues(filterKeysFromOtherServices, currentFilters);

        return getValuesInCurrentService(formDto.getMainIdField(), filtersToCurrentService, formDto.getPage());
    }
}
